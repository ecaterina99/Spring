package com.server.services;

import com.server.dto.BudgetDTO;
import com.server.dto.MissionParticipantsDTO;
import com.server.models.Budget;
import com.server.models.Mission;
import com.server.models.User;
import com.server.repositories.BudgetRepository;
import com.server.repositories.MissionRepository;
import com.server.repositories.UserRepository;
import com.server.util.MissionResult;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ModelMapper modelMapper;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, MissionRepository missionRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.missionRepository = missionRepository;
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }
    public BudgetDTO createInitialBudget(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Optional<Budget> existingBudget = budgetRepository.findByUserId(userId);
        if (existingBudget.isPresent()) {
            return modelMapper.map(existingBudget.get(), BudgetDTO.class);
        }

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCurrentBudget(1000000);

        budgetRepository.save(budget);
        return modelMapper.map(budget, BudgetDTO.class);
    }

    @Transactional(readOnly = true)
    public BudgetDTO getBudgetById(int id) {
        return budgetRepository.findById(id)
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Budget with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public BudgetDTO getUserBudget(Integer userId) {
        Budget budget = budgetRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for user: " + userId));
        return modelMapper.map(budget, BudgetDTO.class);
    }

    @Transactional(readOnly = true)
    public List<BudgetDTO> getAllBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .toList();
    }

    public BudgetDTO updateBudget(Integer userId, Integer missionId, MissionResult missionResult) {
        Budget budget = budgetRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
                    Budget newBudget = new Budget();
                    newBudget.setUser(user);
                    newBudget.setCurrentBudget(1000000);
                    return budgetRepository.save(newBudget);
                });

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found with id: " + missionId));

        int salary = mission.getPaymentAmount();
        int previousBalance = budget.getCurrentBudget();
        int newBalance;

        if (missionResult.isSuccess()) {
            newBalance = previousBalance + salary;
            System.out.println("Mission SUCCESS! User " + userId +
                    " - Previous balance: " + previousBalance +
                    ", Earned: +" + salary +
                    ", New balance: " + newBalance);
        } else {
            newBalance = previousBalance - salary;
            System.out.println("Mission FAILED! User " + userId +
                    " - Previous balance: " + previousBalance +
                    ", Lost: -" + salary +
                    ", New balance: " + newBalance);
        }

        budget.setCurrentBudget(newBalance);
        budgetRepository.save(budget);

        return modelMapper.map(budget, BudgetDTO.class);
    }

}