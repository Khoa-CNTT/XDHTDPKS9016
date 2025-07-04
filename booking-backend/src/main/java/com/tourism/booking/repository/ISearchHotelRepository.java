package com.tourism.booking.repository;
import com.tourism.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ISearchHotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE " +
            "LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(h.address) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Hotel> searchByNameAndAddress(@Param("name") String name);
}
