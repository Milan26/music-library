package project.pa165.musiclibrary.dao;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import project.pa165.musiclibrary.entities.Album;
import project.pa165.musiclibrary.exception.PersistenceException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Milan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-dao-test.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class AlbumDaoImplTest {

    private Album album1;
    private Album album2;

    private AlbumDao albumDao;

    public AlbumDao getAlbumDao() {
        return albumDao;
    }

    @Inject
    public void setAlbumDao(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    @Before
    public void setUp() {
        album1 = createAlbum("Unity", new LocalDate(1991, 2, 6).toDate(), "http://pathtocoverart.com", "album");
        album2 = createAlbum("Hello", new LocalDate(2001, 1, 1).toDate(), "http://blabla.com", "I am hungry");
    }

    @Test
    public void testCreateAlbum() throws PersistenceException {
        persist(album1);
    }

    @Test
    public void testCreatedAlbumProperties() throws PersistenceException {
        persist(album1);

        Album actual = getAlbumDao().find(album1.getId());
        deepAssert(album1, actual);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateAlbumNull() throws PersistenceException {
        getAlbumDao().create(null);
    }

    @Test
    public void testDeleteAlbum() throws PersistenceException {
        persist(album1);

        getAlbumDao().delete(album1.getId());
        assertNull(getAlbumDao().find(album1.getId()));
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteAlbumNull() throws PersistenceException {
        getAlbumDao().delete(null);
    }

    @Test
    public void testUpdateAlbum() throws PersistenceException {
        persist(album1);

        Album actual = getAlbumDao().find(album1.getId());
        assertNotEquals("http://updatedcoverart.com", actual.getCoverArt());
        assertNotEquals("Updated note", actual.getNote());

        album1.setCoverArt("http://updatedcoverart.com");
        album1.setNote("Updated note");

        getAlbumDao().update(album1);
        actual = getAlbumDao().find(album1.getId());

        deepAssert(album1, actual);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateAlbumNull() throws PersistenceException {
        getAlbumDao().update(null);
    }

    @Test
    public void testFindAlbum() throws PersistenceException {
        persist(album1);

        Album actual = getAlbumDao().find(album1.getId());
        assertNotNull(actual);
        assertEquals(album1, actual);
    }

    @Test(expected = PersistenceException.class)
    public void testFindAlbumWithNullId() throws PersistenceException {
        getAlbumDao().find(album1.getId());
    }

    @Test
    public void testFindAlbumWithWrongId() throws PersistenceException {
        assertNull(getAlbumDao().find(5l));
    }

    @Test
    public void testGetAllAlbums() throws PersistenceException {
        persist(album1);
        persist(album2);

        List<Album> expected = Arrays.asList(album1, album2);
        assertEquals(expected.size(), 2);
        List<Album> actual = getAlbumDao().getAll();
        assertEquals(actual.size(), 2);

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testGetAllAlbumsEmptyDb() throws PersistenceException {
        List<Album> actual = getAlbumDao().getAll();
        assertEquals(0, actual.size());
    }

    @Test
    public void testFindAlbumByTitleWithUniqueTitle() throws PersistenceException {
        persist(album1);
        persist(album2);

        album1.setTitle("I am Unique");
        album2.setTitle("I am not");
        getAlbumDao().update(album1);
        getAlbumDao().update(album2);

        assertEquals(getAlbumDao().getAll().size(), 2);

        List<Album> expected = Arrays.asList(album1);
        List<Album> actual = getAlbumDao().findAlbumByTitle("unique");

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindAlbumByTitleWithMultipleSameTitleAlbum() throws PersistenceException {
        persist(album1);
        persist(album2);

        album1.setTitle("I am Unique");
        album2.setTitle("I am not so UniQuE, or maybe I am");
        getAlbumDao().update(album1);
        getAlbumDao().update(album2);

        assertEquals(getAlbumDao().getAll().size(), 2);

        List<Album> expected = Arrays.asList(album1, album2);
        List<Album> actual = getAlbumDao().findAlbumByTitle("unique");

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindAlbumByTitleWithAnyMatch() throws PersistenceException {
        persist(album1);
        persist(album2);

        assertEquals(getAlbumDao().getAll().size(), 2);

        List<Album> expected = new ArrayList<>();
        List<Album> actual = getAlbumDao().findAlbumByTitle("alhamra");

        assertArrayEquals(expected.toArray(), actual.toArray());
        deepAssert(expected.toArray(), actual.toArray());
    }

    @Test
    public void testFindAlbumByTitleOnEmptyDb() throws PersistenceException {
        List<Album> actual = getAlbumDao().getAll();
        assertEquals(actual.size(), 0);

        assertEquals(getAlbumDao().findAlbumByTitle("Unity").size(), 0);
    }

    private void persist(Album album) throws PersistenceException {
        assertNull(album.getId());
        getAlbumDao().create(album);
        assertNotNull(album.getId());
    }

    private Album createAlbum(String title, Date releaseDate, String coverArt, String note) {
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.setCoverArt(coverArt);
        album.setNote(note);
        return album;
    }

    private void deepAssert(Album album1, Album album2) {
        assertEquals(album1.getId(), album2.getId());
        assertEquals(album1.getTitle(), album2.getTitle());
        assertEquals(album1.getReleaseDate(), album2.getReleaseDate());
        assertEquals(album1.getCoverArt(), album2.getCoverArt());
        assertEquals(album1.getNote(), album2.getNote());
    }

    private void deepAssert(Object[] arr1, Object[] arr2) {
        assertEquals(arr1.length, arr2.length);

        for (int i = 0; i < arr1.length; i++) {
            Album album1 = (Album) arr1[i];
            Album album2 = (Album) arr2[i];
            deepAssert(album1, album2);
        }
    }
}