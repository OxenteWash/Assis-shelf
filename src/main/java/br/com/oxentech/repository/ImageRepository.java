package br.com.oxentech.repository;

import br.com.oxentech.domain.Book;
import br.com.oxentech.domain.Image;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {}
