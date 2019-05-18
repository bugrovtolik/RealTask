package com.abugrov.realtask.service;

import com.abugrov.realtask.model.Contract;
import com.abugrov.realtask.model.Task;
import com.abugrov.realtask.model.User;
import com.abugrov.realtask.repos.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {
    private final ContractRepo contractRepo;

    @Autowired
    public ContractService(ContractRepo contractRepo) {
        this.contractRepo = contractRepo;
    }

    public Contract findByUserAndTask(User user, Task task) {
        return contractRepo.findByUserAndTask(user, task);
    }

    public List<Contract> findByUser(User user) {
        return contractRepo.findByUser(user);
    }

    public List<Contract> findByTask(Task task) {
        return contractRepo.findByTask(task);
    }

    public Contract findByTaskAndAccepted(Task task) {
        return contractRepo.findByTaskAndAccepted(task, true);
    }

    public void saveContract(Contract contract) {
        contractRepo.save(contract);
    }

    public void deleteByTaskAndNotAccepted(Task task) {
        contractRepo.deleteByTaskAndAccepted(task, false);
    }

    public void deleteContract(Contract contract) {
        contractRepo.delete(contract);
    }

    public void acceptContract(Contract contract) {
        contract.setAccepted(true);
        contractRepo.save(contract);
    }

    public void deleteByTask(Task task) {
        contractRepo.deleteByTask(task);
    }

    public void declineAllExcept(Contract contract) {
        contractRepo.declineAllInExcept(contract.getTask(), contract.getId());
    }

    public Contract findByTaskAndCompleted(Task task) {
        return contractRepo.findByTaskAndCompleted(task, true);
    }

    public List<Contract> check(List<Contract> list) {
        for (Contract contract : list) {
            if (!contract.getTask().isActive()) {
                contract.setCompleted(false);
                saveContract(contract);
            }
        }

        return list;
    }
}
