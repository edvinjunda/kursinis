package hibernateControllers;

import books.Book;
import books.Cart;
import users.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import static incorrectDataControl.IncorrectDataControl.alertMsg;

public class CartHibernateCtrl {
    private EntityManagerFactory emf = null;

    public CartHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEm() {
        return emf.createEntityManager();
    }

    public void createCart(Cart cart) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.persist(cart); //INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void updateCart(Cart cart) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.merge(cart); //UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public void removeCart(int id) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            Cart cart = null;
            try {
                cart = em.getReference(Cart.class, id);
                cart.getId();
            } catch (Exception e) {
                System.out.println("Cart doesn't exist");
            }

            //book = em.find(Book.class, id);

            em.remove(cart);
            em.getTransaction().commit();
        } catch (Exception e) {
            //e.printStackTrace();
            alertMsg("This cart is unavailable.","It was already removed.");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List getAllCarts() {
        EntityManager em = getEm();
        try {
            CriteriaQuery<Object> query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Cart.class));
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

    public Cart getCartById(int id) {
        EntityManager em = null;
        Cart cart = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            cart = em.find(Cart.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User with entered Id doesn't exist");
        }
        return cart;
    }

    public List getCartByBuyer(User user){
        EntityManager em = getEm();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Cart> query = cb.createQuery(Cart.class);
            Root<Cart> root = query.from(Cart.class);
            query.select(root).where(cb.equal(root.get("buyer"),user));
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
