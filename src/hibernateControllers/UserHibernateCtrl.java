package hibernateControllers;

import users.Company;
import users.Person;
import users.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static incorrectDataControl.IncorrectDataControl.alertMsg;

public class UserHibernateCtrl {

    private EntityManagerFactory emf = null;

    public UserHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEm() {
        return emf.createEntityManager();
    }

    /**
     * Inserts new User to db
     *
     * @param user
     */
    public void createUser(User user) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.persist(user); //INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();////alertMsg?
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * updates existing user
     *
     * @param user
     */
    public void updateUser(User user) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.merge(user); //UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * removes existing user
     *
     * @param id
     */
    public void removeUser(int id) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            User user= null;
            try {
                user = em.getReference(User.class,id);
                user.getId();
            }
            catch (Exception e){
                System.out.println("User with entered Id doesn't exist");
            }
            em.remove(user); //REMOVE
            em.getTransaction().commit();
        } catch (Exception e) {
            //e.printStackTrace();
            alertMsg("This user is unavailable.","The user was already removed.");
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * Selects all users from db
     *
     * @return
     */
    public List getAllUsers() {
        return getAllUsers(false, -1, -1);
    }

    public List getAllUsers(boolean all, int resMax, int resFirst) {
        EntityManager em = getEm();
        try {
            CriteriaQuery<Object> query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(User.class));
            Query q = em.createQuery(query);

            if (!all) {
                q.setMaxResults(resMax);
                q.setFirstResult(resFirst);
            }
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

    /**
     * get all people (DTYPE = Person)
     *
     * @return
     */
    public List getAllPerson() {
        EntityManager em = getEm();
        try {
            CriteriaQuery<Object> query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Person.class));
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

    /**
     * get all companies (DTYPE = Company)
     *
     * @return
     */
    public List getAllCompany() {
        EntityManager em = getEm();
        try {
            CriteriaQuery<Object> query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Company.class));
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

    /**
     * gets user by provided id
     *
     * @param id
     * @return
     */
    public User getUserById(int id) {
        EntityManager em = null;
        User user = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            user = em.find(User.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User with entered Id doesn't exist");
        }
        return user;
    }

    public User getPersonById(int id) {
        EntityManager em = null;
        Person person = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            person = em.find(Person.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User with entered Id doesn't exist");
        }
        return person;
    }

    public User getCompanyById(int id) {
        EntityManager em = null;
        Company company = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            company = em.find(Company.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User with entered Id doesn't exist");
        }
        return company;
    }

    /**
     * select user by given credentials
     *
     * @param login
     * @param password
     * @return
     */
    public User getUserByLoginData(String login, String password) {
        EntityManager em = getEm();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
        Query q;
        try {
            q = em.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserByLogin(String login) {
        EntityManager em = getEm();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(cb.like(root.get("login"), login));
        Query q;
        try {
            q = em.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
