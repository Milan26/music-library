package project.pa165.musiclibrary.dao;

import project.pa165.musiclibrary.entities.Artist;
import project.pa165.musiclibrary.exception.DaoException;

import javax.inject.Named;
import java.util.List;

/**
 * Implementation of ArtistDao.
 *
 * @author Matúš
 */
@Named
public class ArtistDaoImpl extends AbstractGenericDao<Artist> implements ArtistDao {

    /**
     * Sets specific type for genericDao.
     */
    public ArtistDaoImpl() {
        super();
        setType(Artist.class);
    }

    @Override
    public List<Artist> findArtistByName(final String name) throws DaoException {
        try {
            return getEntityManager().createQuery("SELECT a FROM Artist a WHERE lower(a.name) like lower('%" + name +
                    "%') ", Artist.class).getResultList();
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

}
