/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne.utilities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author korbinus
 */
public class Db {

    /**
     * <p>Return a single object from a table</p>
     *
     * @param <T>
     * @param em entityManager
     * @param namedQuery the named query to be run
     * @param parameterName the parameter name
     * @param parameter the parameter value
     * @return
     */
    public static <T> T getSingleObject(EntityManager em, String namedQuery, String parameterName, Object parameter) {
        Query q = em.createNamedQuery(namedQuery)
                .setParameter(parameterName, parameter)
                .setMaxResults(1)
                .setHint("eclipselink.read-only", true);
        List<T> result = q.getResultList();
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
