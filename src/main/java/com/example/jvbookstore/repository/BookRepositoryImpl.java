package com.example.jvbookstore.repository;

import com.example.jvbookstore.exception.DataProcessingException;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.model.Book;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);
    private final SessionFactory sessionFactory;
    private final LocalContainerEntityManagerFactoryBean entityManagerFactory2;

    @Override
    public Book save(Book book) {
        Session session;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert user into DB: " + book);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Book getBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory2
                .createNativeEntityManager(Collections.emptyMap())) {
            Book book = entityManager.find(Book.class, id);
            if (book == null) {
                throw new EntityNotFoundException("Book not found with id: " + id);
            }
            return book;
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        } catch (HibernateException ex) {
            throw new DataProcessingException("Failed to fetch books", ex);
        }
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        String lowerCaseTitle = title.toLowerCase();
        try (EntityManager entityManager = entityManagerFactory2
                .createNativeEntityManager(Collections.emptyMap())) {
            return entityManager
                    .createQuery("SELECT e FROM Book e WHERE lower(e.title) LIKE : name",
                            Book.class)
                    .setParameter("name", "%" + lowerCaseTitle + "%")
                    .getResultList();
        }
    }

}
