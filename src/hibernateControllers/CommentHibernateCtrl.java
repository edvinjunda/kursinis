package hibernateControllers;

import books.Comment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CommentHibernateCtrl {

    private EntityManagerFactory emf = null;
    public CommentHibernateCtrl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManager getEm() {
        return emf.createEntityManager();
    }


    public Comment getCommentById(int id) {
        EntityManager em = null;
        Comment comment = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            comment = em.find(Comment.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Comment by given Id can't be found");
        }
        return comment;
    }

    public List getAllComments() {
        EntityManager em = getEm();
        try {
            CriteriaQuery<Object> query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Comment.class));
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

    public void editComment(Comment comment) {
        EntityManager em = null;
        try {
            em = getEm();
            em.getTransaction().begin();
            em.merge(comment); //UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeComment(int id){
        EntityManager em = null;
        try{
            em = getEm();
            em.getTransaction().begin();
            Comment comment = null;
            try{
                comment = em.getReference(Comment.class, id);
                comment.getId();
            }
            catch (Exception e){
                System.out.println("Comment by given Id can't be found");
            }
            em.remove(comment);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em != null){
                em.close();
            }
        }
    }
}
