package hibernateControllers;

import books.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static incorrectDataControl.IncorrectDataControl.alertMsg;

public class BookHibernateCtrl {

    private EntityManagerFactory emf = null;

    public BookHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEm() {
        return emf.createEntityManager();
    }


    public void createBook(Book book) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.persist(book); //INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void updateBook(Book book) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.merge(book); //UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void removeBook(int id) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            Book book = null;
            try {
                book = em.getReference(Book.class, id);
                book.getId();
            } catch (Exception e) {
                System.out.println("Book with entered Id doesn't exist");
            }

            //book = em.find(Book.class, id);

            em.remove(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            //e.printStackTrace();
            alertMsg("This book can't be removed.","It was already removed or is contained in some cart.");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List getAllBooks(int inStock) {
        EntityManager em = getEm();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            if (inStock==0)
                query.select(root).where(cb.gt(root.get("inStock"), inStock));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return new ArrayList();
    }

    public Book getBookById(int id) {
        EntityManager em = null;
        Book book = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            book = em.find(Book.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Book with entered Id doesn't exist");
        }
        return book;
    }

    public List getFilteredBooks(String title, String authors, LocalDate publishDateStart, LocalDate publishDateEnd, int inStock) {
        if(publishDateStart == null) publishDateStart = LocalDate.parse("1500-01-01");
        if(publishDateEnd == null) publishDateEnd = LocalDate.parse("2200-01-01");
        EntityManager em = getEm();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> query = cb.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);
            query.select(root).where(cb.and(cb.like(root.get("bookTitle"), "%" + title + "%")), cb.like(root.get("authors"), "%" + authors + "%"), cb.between(root.get("publishDate"), publishDateStart, publishDateEnd), cb.gt(root.get("inStock"), inStock));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }




}
