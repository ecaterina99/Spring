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

/**
 * Service class that manages business logic for Budget entities.
 * It handles CRUD operations (create, read, update),
 * maps between Budget and BudgetDTO objects,
 * and interacts with the BudgetRepository for database access.
 * Calculates total mission expenses
 */
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

    public void createInitialBudget(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " is not found"));

        Optional<Budget> existingBudget = budgetRepository.findByUserId(userId);
        if (existingBudget.isPresent()) {
            modelMapper.map(existingBudget.get(), BudgetDTO.class);
            return;
        }

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCurrentBudget(1000000);

        budgetRepository.save(budget);
        modelMapper.map(budget, BudgetDTO.class);
    }

    @Transactional(readOnly = true)
    public BudgetDTO getBudgetById(int id) {
        return budgetRepository.findById(id)
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Budget with id: " + id + "is not found"));
    }

    @Transactional(readOnly = true)
    public BudgetDTO getUserBudget(Integer userId) {
        Budget budget = budgetRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for user with id: " + userId));
        return modelMapper.map(budget, BudgetDTO.class);
    }

    @Transactional(readOnly = true)
    public List<BudgetDTO> getAllBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .toList();
    }
    @Transactional
    public BudgetDTO updateBudget(Integer userId, Integer missionId, MissionResult missionResult, List<MissionParticipantsDTO> participants) {
        Budget budget = budgetRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " is not found"));
                    Budget newBudget = new Budget();
                    newBudget.setUser(user);
                    newBudget.setCurrentBudget(1000000);
                    return budgetRepository.save(newBudget);
                });

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission with id: " + missionId + " is not found"));

        int missionExpanses = calculateMissionExpenses(missionId, participants);
        int paymentForMission = mission.getPaymentAmount();

        int previousBalance = budget.getCurrentBudget();
        int newBalance;

        if (previousBalance < missionExpanses) {
            System.out.println("Unfortunately, you don't have enough money to pay for this mission. Current budget: "
                    + previousBalance + "; Payment for mission: " + missionExpanses);
            newBalance = previousBalance;
        } else {
            if (missionResult.isSuccess()) {
                newBalance = previousBalance + paymentForMission - missionExpanses;
                System.out.println("Mission SUCCESS! User " + userId +
                        " - Previous balance: " + previousBalance +
                        ", Earned: +" + missionExpanses +
                        ", New balance: " + newBalance);
            } else {
                newBalance = previousBalance - missionExpanses;
                System.out.println("Mission FAILED! User " + userId +
                        " - Previous balance: " + previousBalance +
                        ", Lost: -" + missionExpanses +
                        ", New balance: " + newBalance);

            }
        }
        budget.setCurrentBudget(newBalance);
        budgetRepository.save(budget);
        return modelMapper.map(budget, BudgetDTO.class);
    }

    protected int calculateMissionExpenses(Integer missionId, List<MissionParticipantsDTO> participants) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission with id: " + missionId + " is not found"));

        int totalExpenses = 0;
        for (MissionParticipantsDTO participant : participants) {
            System.out.println("Participant daily rate:" + participant.getDailyRate());
            System.out.println("Mission duration:" + mission.getDurationDays());
            totalExpenses = participant.getDailyRate() * mission.getDurationDays();
            totalExpenses++;
        }
        return totalExpenses;
    }
}