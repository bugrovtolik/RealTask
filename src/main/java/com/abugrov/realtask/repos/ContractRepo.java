package com.abugrov.realtask.repos;

import com.abugrov.realtask.domain.Contract;
import com.abugrov.realtask.domain.Task;
import com.abugrov.realtask.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContractRepo extends JpaRepository<Contract, Long> {

    List<Contract> findByTask(Task task);

    void deleteByTask(Task task);

    Contract findByUserAndTask(User user, Task task);

    List<Contract> findByUserAndAccepted(User user, boolean b);

    List<Contract> findByUser(User user);

    Contract findByTaskAndAccepted(Task task, boolean b);

    void deleteByTaskAndAccepted(Task task, boolean b);

    @Transactional
    @Modifying
    @Query("update Contract c set c.accepted = false where c.id <> :id and c.task = :task")
    void declineAllInExcept(@Param("task") Task task, @Param("id") Long id);

    Contract findByTaskAndCompleted(Task task, boolean b);
}
