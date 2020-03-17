package com.ningmeng.order.dao;

import com.ningmeng.framework.domain.task.NmTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface NmTaskRepository extends JpaRepository<NmTask,String> {

    //取出指定时间之前的记录
    Page<NmTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    //更新任务处理时间
    @Modifying
    @Query(value = "update NmTask t set t.updateTime = :updateTime where t.id = :id")
    public int updateTaskTime(@Param(value = "id")String id,@Param(value = "updateTime")Date updateTime);


    //使用乐观锁方式效验任务id 和版本号是否匹配，匹配则版本号加1
    @Modifying
    @Query(value = "update NmTask t set t.version = :version + 1 where t.id = :id")
    public int updateTaskVersion(@Param(value = "id")String id,@Param(value = "version")int version);




}
