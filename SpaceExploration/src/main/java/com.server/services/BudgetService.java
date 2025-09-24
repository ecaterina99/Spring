package com.server.services;

import com.server.dto.BudgetDTO;
import com.server.models.Budget;
import com.server.repositories.BudgetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ModelMapper modelMapper;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
        this.modelMapper = new ModelMapper();
    }
    @Transactional(readOnly = true)
    public BudgetDTO getBudgetById(int id) {
        return budgetRepository.findById(id)
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Budget with id: " + id + " not found"));
    }
    @Transactional(readOnly = true)
    public List<BudgetDTO> getAllBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .toList();
    }
}