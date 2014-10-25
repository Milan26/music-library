package project.pa165.musiclibrary.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import project.pa165.musiclibrary.entities.Artist;
import project.pa165.musiclibrary.exception.DaoException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-persistence-test.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ArtistDaoImplTest {

    private Artist artist1;
    private Artist artist2;

    private ArtistDao artistDao;

    public ArtistDao getArtistDao() {
        return artistDao;
    }

    @Inject
    public void setArtistDao(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Before
    public void setUp() {
        artist1 = createArtist("Alfa", "Testing artist");
        artist2 = createArtist("Beta", "Bingo JUNIT HALO3");
    }

    @Test
    public void testCreateArtist() throws DaoException {
        persist(artist1);
    }

    @Test
    public void testCreatedArtistProperties() throws DaoException {
        persist(artist1);

        Artist actual = getArtistDao().find(artist1.getId());
        deepAssert(artist1, actual);
    }

    @Test(expected = DaoException.class)
    public void testCreateArtistNull() throws DaoException {
        getArtistDao().create(null);
    }

    @Test
    public void testDeleteArtist() throws DaoException {
        persist(artist1);

        getArtistDao().delete(artist1.getId());
        assertNull(getArtistDao().find(artist1.getId()));
    }

    @Test(expected = DaoException.class)
    public void testDeleteArtistNull() throws DaoException {
        getArtistDao().delete(null);
    }

    @Test
    public void testUpdateArtist() throws DaoException {
        persist(artist1);

        Artist actual = getArtistDao().find(artist1.getId());
        assertNotEquals("Updated", actual.getName());
        assertNotEquals("Updated note", actual.getNote());

        artist1.setName("Updated");
        artist1.setNote("Updated note");

        getArtistDao().update(artist1);
        actual = getArtistDao().find(artist1.getId());

        deepAssert(artist1, actual);
    }

    @Test(expected = DaoException.class)
    public void testUpdateArtistNull() throws DaoException {
        getArtistDao().update(null);
    }

    @Test
    public void testFindArtist() throws DaoException {
        persist(artist1);

        Artist actual = getArtistDao().find(artist1.getId());
        assertNotNull(actual);
        assertEquals(artist1, actual);
    }

    @Test(expected = DaoException.class)
    public void testFindArtistWithNullId() throws DaoException {
        getArtistDao().find(artist1.getId());
    }

    @Test
    public void testFindArtistWithWrongId() throws DaoException {
        assertNull(getArtistDao().find(5l));
    }

    @Test
    public void testGetAllArtists() throws DaoException {
        persist(artist1);
        persist(artist2);

        List<Artist> expected = Arrays.asList(artist1, artist2);
        assertEquals(expected.size(), 2);
        List<Artist> actual = getArtistDao().getAll();
        assertEquals(actual.size(), 2);

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testGetAllArtistsEmptyDb() throws DaoException {
        List<Artist> expected = new ArrayList<>();
        assertEquals(expected.size(), 0);
        List<Artist> actual = getArtistDao().getAll();
        assertEquals(actual.size(), 0);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindArtistByNameWithUniqueName() throws DaoException {
        persist(artist1);
        persist(artist2);

        artist1.setName("I am Unique");
        artist2.setName("I am not");
        getArtistDao().update(artist1);
        getArtistDao().update(artist2);

        assertEquals(getArtistDao().getAll().size(), 2);

        List<Artist> expected = Arrays.asList(artist1);
        List<Artist> actual = getArtistDao().findArtistByName("unique");

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindArtistByNameWithMultipleSameNameArtist() throws DaoException {
        persist(artist1);
        persist(artist2);

        artist1.setName("I am Unique");
        artist2.setName("I am not so UniQuE, or maybe I am");
        getArtistDao().update(artist1);
        getArtistDao().update(artist2);

        assertEquals(getArtistDao().getAll().size(), 2);

        List<Artist> expected = Arrays.asList(artist1, artist2);
        List<Artist> actual = getArtistDao().findArtistByName("unique");

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindArtistByNameWithAnyMatch() throws DaoException {
        persist(artist1);
        persist(artist2);

        assertEquals(getArtistDao().getAll().size(), 2);

        List<Artist> expected = new ArrayList<>();
        List<Artist> actual = getArtistDao().findArtistByName("alhamra");

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindArtistByNameOnEmptyDb() throws DaoException {
        List<Artist> expected = new ArrayList<>();
        assertEquals(expected.size(), 0);
        List<Artist> actual = getArtistDao().getAll();
        assertEquals(actual.size(), 0);

        assertEquals(getArtistDao().findArtistByName("Unity").size(), 0);
    }

    private void persist(Artist artist) throws DaoException {
        assertNull(artist.getId());
        getArtistDao().create(artist);
        assertNotNull(artist.getId());
    }

    private Artist createArtist(String name, String note) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setNote(note);
        return artist;
    }

    private void deepAssert(Artist artist1, Artist artist2) {
        assertEquals(artist1.getId(), artist2.getId());
        assertEquals(artist1.getName(), artist2.getName());
        assertEquals(artist1.getNote(), artist2.getNote());
    }

    private void deepAssert(Object[] arr1, Object[] arr2) {
        assertEquals(arr1.length, arr2.length);

        for (int i = 0; i < arr1.length; i++) {
            Artist artist1 = (Artist) arr1[i];
            Artist artist2 = (Artist) arr2[i];
            deepAssert(artist1, artist2);
        }
    }
}