package com.nyaina.videocall.repositories;

import com.nyaina.videocall.models.Notif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifRepository extends JpaRepository<Notif , Long> {
}
