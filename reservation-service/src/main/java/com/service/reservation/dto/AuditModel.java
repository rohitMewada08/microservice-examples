package com.service.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public abstract class AuditModel implements Serializable {
    private Date createdAt;
    private Date updatedAt;

}
