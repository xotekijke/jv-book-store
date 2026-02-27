package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);
    private final SessionFactory sessionFactory;

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
            throw new RuntimeException("Can't insert user into DB: " + book);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        } catch (HibernateException ex) {
            throw new RuntimeException("Failed to fetch books", ex);
        }
    }
}
