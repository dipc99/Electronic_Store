package com.lcwt.electronic.store.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ToString
public class CustomFieldsDto {

    private String isactive;
    @CreatedBy
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @LastModifiedBy
    private String lastModifiedBy;
    @UpdateTimestamp
    private LocalDateTime modifiedOn;


}
