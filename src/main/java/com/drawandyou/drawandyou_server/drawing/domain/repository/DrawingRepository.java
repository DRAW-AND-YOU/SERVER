package com.drawandyou.drawandyou_server.drawing.domain.repository;

import com.drawandyou.drawandyou_server.drawing.domain.entity.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Long> {

}
