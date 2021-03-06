package ru.lesson.springBootProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import ru.lesson.springBootProject.dto.BookDto;
import ru.lesson.springBootProject.exceptions.BookServiceException;
import ru.lesson.springBootProject.models.*;
import ru.lesson.springBootProject.repositories.BookRepository;
import ru.lesson.springBootProject.repositories.CommentBookRepository;
import ru.lesson.springBootProject.repositories.GenreRepository;
import ru.lesson.springBootProject.services.filters.FilterBook;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentBookRepository commentBookRepository;
    @Override
    public Book save(Book book){
        return bookRepository.save(book);
    }

    @Override
    public Page<BookDto> findAll(Long userId, Pageable pageable){
        Page<BookDto> resultPage = bookRepository.findAll(userId, pageable);
        if (resultPage.getNumber()>=resultPage.getTotalPages()){
            pageable= PageRequest.of(resultPage.getTotalPages()-1,pageable.getPageSize(),pageable.getSort());
            resultPage = findAll(userId,pageable);
        }
        return resultPage;
    }

    @Override
    public Book like(Book book, User user){
        if (book.getLikes().contains(user)){
            book.getLikes().remove(user);
        }else{
            book.getLikes().add(user);
        }
        return bookRepository.save(book);
    }

    @Override
    public void changeGenre(Book book, String[] genresName){

        //приводим массив String к Set<GenreName>(набор допустимых жанров согласно enum)
        Set<GenreName> newGenres = Arrays.stream(genresName).map(genreName->GenreName.valueOf(genreName)).collect(Collectors.toSet());
        //нужно пройти по множеству старых жанров(которые сейчас в бд) и убрать те которых нет среди вновь выбранных жанров
        //при этом если среди старых жанров есть вновь выбранные, то нужно удалить их из новых, чтобы не было попытки дважды включить в бд одну и ту же запись
        Iterator<GenreBook> iteratorGenres = book.getGenres().iterator();
        while (iteratorGenres.hasNext()){
            GenreBook nextGenreBook = iteratorGenres.next();
            if (!newGenres.contains(nextGenreBook.getGenre())){
                iteratorGenres.remove();
            }else{
                newGenres.remove(nextGenreBook);
            }
        }
        //добавляем новые жанры в список старых, при этом приводим из GenreName->Genre
        for (GenreName newGenreName : newGenres){
            book.getGenres().add(GenreBook.builder().genre(newGenreName).book(book).build());
        }

    }

    @Override
    public void delete(Long bookId) {
        Optional<Book> candidateBook = bookRepository.findById(bookId);
        if (candidateBook.isEmpty()) return;
        bookRepository.delete(candidateBook.get());
    }

    //метод получения списка книг.
    @Override
    public Page<BookDto> findAllWithFilter(Long userId, Pageable pageable, FilterBook filterBook) {
        Page<BookDto> page =null;
        if (filterBook !=null){
            //если задана сортировка по количеству лайков, то меняем атрибут сортировки у Pageable.
            //если сортировка не задана, но filterBook!=null, что возможно только в случае если задан жанр для фильтрации,
            // то нужно установить сортировку по умолчанию, чтобы избежать ошибок
            if (filterBook.getSortedByLikes()!=null&&filterBook.getSortedByLikes()!= FilterBook.SortedByLikes.NONE){
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), JpaSort.unsafe(Sort.Direction.valueOf(filterBook.getSortedByLikes().name()), "count(lk)"));

            }else{
            //устанавливаем сортировку по умолчанию
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "bookId"));
            }
            //вызываем метод поиска, который отберёт записи по жанру
            if (filterBook.getGenreName()!=null) {
                page = bookRepository.findAllWithParam(userId, pageable, filterBook.getGenreName());
            }
        }
        //page==null когда не задан Жанр для фильтрации или FilterBook
        if (page ==null) page= bookRepository.findAll(userId,pageable);

        //если текущая страница дальше максимальной, то рекурсивно запрашиваем последнюю возможную страницу
        if (page.getNumber()>=page.getTotalPages())
        {
            pageable= PageRequest.of(page.getTotalPages()-1,pageable.getPageSize(),pageable.getSort());
            page = findAllWithFilter(userId,pageable, filterBook);
        }
        return page;
    }

    @Override
    public Page<BookDto> findAllByAuthors(Long userId,Long authorId, Pageable pageable) {
        Page<BookDto> resultPage = bookRepository.findAllByAuthors(userId, authorId, pageable);
        if (resultPage.getNumber()>=resultPage.getTotalPages()){
            pageable= PageRequest.of(resultPage.getTotalPages()-1,pageable.getPageSize(),pageable.getSort());
            resultPage = findAllByAuthors(userId,authorId,pageable);
        }
        return resultPage;
    }

    @Override
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseGet(null);
    }

    @Override
    public Page<BookDto> findAllLikes(Long userId, Pageable pageable) {
        Page<BookDto> resultPage = bookRepository.findAllByLikes(userId, pageable);
        if (resultPage.getNumber()>=resultPage.getTotalPages()){
            pageable= PageRequest.of(resultPage.getTotalPages()-1,pageable.getPageSize(),pageable.getSort());
            resultPage = findAllLikes(userId,pageable);
        }
        return resultPage;
    }

    @Override
    public Page<CommentBook> findAllComments(Long bookId, Pageable pageable) {
        Page<CommentBook> resultPage = commentBookRepository.findAllByBook_BookId(bookId, pageable);
        if (resultPage.getNumber()>=resultPage.getTotalPages()){
            pageable= PageRequest.of(resultPage.getTotalPages()-1,pageable.getPageSize(),pageable.getSort());
            resultPage = findAllComments(bookId,pageable);
        }
        return resultPage;
    }

    @Override
    public BookDto findBookDtoById(Long userId, Long bookId) {
        return bookRepository.findBookDtoById(userId,bookId);
    }
}
