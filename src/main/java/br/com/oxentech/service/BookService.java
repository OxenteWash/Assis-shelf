package br.com.oxentech.service;

import br.com.oxentech.domain.Book;
import br.com.oxentech.domain.Image;
import br.com.oxentech.repository.BookRepository;
import br.com.oxentech.repository.ImageRepository;
import br.com.oxentech.service.dto.BookDTO;
import br.com.oxentech.service.dto.ImageDTO;
import br.com.oxentech.service.mapper.BookMapper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link br.com.oxentech.domain.Book}.
 */
@Service
@Transactional
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final ImageRepository imageRepository;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.imageRepository = imageRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    public BookDTO save(BookDTO bookDTO) {
        LOG.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    /**
     * Update a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    public BookDTO update(BookDTO bookDTO) {
        LOG.debug("Request to update Book : {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    /**
     * Partially update a book.
     *
     * @param bookDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookDTO> partialUpdate(BookDTO bookDTO) {
        LOG.debug("Request to partially update Book : {}", bookDTO);

        return bookRepository
            .findById(bookDTO.getId())
            .map(existingBook -> {
                bookMapper.partialUpdate(existingBook, bookDTO);

                return existingBook;
            })
            .map(bookRepository::save)
            .map(bookMapper::toDto);
    }

    /**
     * Get all the books.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        LOG.debug("Request to get all Books");
        return bookRepository.findAll().stream().map(bookMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the books with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }
}
