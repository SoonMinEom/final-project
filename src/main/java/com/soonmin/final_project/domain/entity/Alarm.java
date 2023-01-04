package com.soonmin.final_project.domain.entity;

import com.soonmin.final_project.domain.dto.alarm.AlarmResponse;
import com.soonmin.final_project.domain.dto.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.security.PublicKey;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
@SQLDelete(sql = "UPDATE alarm SET deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Alarm extends PostBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private AlarmType alarmType;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Integer fromUserId;
    private Integer targetId;
    private String text;
    private LocalDateTime deletedAt;

    public static Alarm makeAlarm(AlarmType alarmType, User user, Integer fromUserId, Integer targetId) {
        return Alarm.builder()
                .alarmType(alarmType)
                .user(user)
                .fromUserId(fromUserId)
                .targetId(targetId)
                .text(alarmType.getAlarmText())
                .build();
    }

    public AlarmResponse toResponse() {
        return AlarmResponse.builder()
                .id(this.id)
                .alarmType(this.alarmType)
                .fromUserId(this.fromUserId)
                .targetId(this.targetId)
                .text(this.text)
                .createdAt(getCreatedAt())
                .build();
    }
}
