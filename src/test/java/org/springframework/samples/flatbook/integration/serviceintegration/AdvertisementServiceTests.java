package org.springframework.samples.flatbook.integration.serviceintegration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.samples.flatbook.utils.assertj.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AdvertisementServiceTests {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private FlatService flatService;

    private static Advertisement advertisement;

    @BeforeAll
    static void setup() {
        Address address = new Address();
        address.setCountry("Spain");
        address.setCity("Sevilla");
        address.setPostalCode("41000");
        address.setLocation("Plaza Nueva");

        DBImage image = new DBImage();
        image.setFilename("a.png");
        image.setFileType("image/png");
        image.setData(new byte[]{1, 2, 3});

        DBImage image2 = new DBImage();
        image2.setFilename("b.png");
        image2.setFileType("image/png");
        image2.setData(new byte[]{1, 2, 3});

        DBImage image3 = new DBImage();
        image3.setFilename("c.png");
        image3.setFileType("image/png");
        image3.setData(new byte[]{1, 2, 3});

        DBImage image4 = new DBImage();
        image4.setFilename("d.png");
        image4.setFileType("image/png");
        image4.setData(new byte[]{1, 2, 3});

        DBImage image5 = new DBImage();
        image5.setFilename("e.png");
        image5.setFileType("image/png");
        image5.setData(new byte[]{1, 2, 3});

        DBImage image6 = new DBImage();
        image6.setFilename("f.png");
        image6.setFileType("image/png");
        image6.setData(new byte[]{1, 2, 3});

        Set<DBImage> images = new HashSet<>(Arrays.asList(image, image2, image3, image4, image5, image6));

        Flat flat = new Flat();
        flat.setDescription("this is a sample description with more than 30 characters");
        flat.setSquareMeters(100);
        flat.setNumberRooms(3);
        flat.setNumberBaths(2);
        flat.setAvailableServices("Wifi and TV");
        flat.setAddress(address);
        flat.setImages(images);

        advertisement = new Advertisement();
        advertisement.setTitle("Sample title");
        advertisement.setDescription("Sample description");
        advertisement.setRequirements("Sample requirements");
        advertisement.setPricePerMonth(100.50);
        advertisement.setCreationDate(LocalDate.now());
        advertisement.setFlat(flat);
    }

    @Test
    @Order(1)
    void shouldFindAdvertisementWithFlatId() {
        Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(2);
        assertThat(adv).hasId(2);
        assertThat(adv).hasPricePerMonth(420.);
        assertThat(adv).hasTitle("Advertisement 2");
    }

    @Test
    @Order(2)
    void shouldNotFindAdvertisementWithFlatIdNotInDatabase() {
        Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(0);
        assertThat(adv).isNull();
    }

    @Test
    @Order(3)
    void shouldCheckThereIsAdvertisementWithFlatId() {
        Boolean b = this.advertisementService.isAdvertisementWithFlatId(2);
        Assertions.assertThat(b).isTrue();
    }

    @Test
    @Order(4)
    void shouldCheckThereIsNotAdvertisementWithFlatId() {
        Boolean b = this.advertisementService.isAdvertisementWithFlatId(0);
        Assertions.assertThat(b).isFalse();
    }

    @Test
    @Order(5)
    void shouldFindAdvertisementsByHost() {
        Set<Advertisement> advertisements = this.advertisementService.findAdvertisementsByHost("rbordessa0");
        Assertions.assertThat(advertisements).hasSize(2);
        Assertions.assertThat(advertisements).extracting(Advertisement::getTitle)
            .containsExactlyInAnyOrder("Advertisement 16", "Advertisement 31");
    }
    @Test
    @Order(6)
    void shouldNotFindAdvertisementsByHost() {
        Set<Advertisement> advertisements = this.advertisementService.findAdvertisementsByHost("User1");
        Assertions.assertThat(advertisements).isEmpty();
    }
    @Test
    @Order(7)
    void shouldFindAllAdvertisements() {
        Set<Advertisement> advertisements = this.advertisementService.findAllAdvertisements();
        Assertions.assertThat(advertisements).hasSize(44);
    }

    @Test
    @Order(8)
    void shouldFindAdvertisementById() {
        Advertisement advertisement = this.advertisementService.findAdvertisementById(10);
        assertThat(advertisement).hasId(10);
        assertThat(advertisement).hasTitle("Advertisement 10");
        assertThat(advertisement).hasDescription("Nam nulla. Integer pede justo, lacinia eget, tincidunt eget, tempus vel, pede.");
        assertThat(advertisement).hasPricePerMonth(722.);
        assertThat(advertisement).hasRequirements("Sed accumsan felis. Ut at dolor quis odio consequat varius.");
        assertThat(advertisement.getFlat()).isNotNull();
    }

    @Test
    @Order(9)
    void shouldNotFindAdvertisementById() {
        Advertisement advertisement = this.advertisementService.findAdvertisementById(100);
        assertThat(advertisement).isNull();
    }

    @Test
    @Order(10)
    void shouldAddNewAdvertisement() {
        flatService.saveFlat(advertisement.getFlat());
        this.advertisementService.saveAdvertisement(advertisement);

        Advertisement advertisementSaved = this.advertisementService.findAdvertisementById(advertisement.getId());
        assertThat(advertisementSaved).isNotNull();
        assertThat(advertisementSaved).hasTitle(advertisement.getTitle());
        assertThat(advertisementSaved).hasPricePerMonth(advertisement.getPricePerMonth());
    }

    @Test
    @Order(11)
    void shouldThrowExceptionWhenTryingToAddNullAdvertisement() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.advertisementService.saveAdvertisement(null));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null");
    }

    @Test
    @Order(12)
    void shouldDeleteAdvertisement() {
        int advertisementId = advertisement.getId();
        this.advertisementService.deleteAdvertisement(advertisement);

        Advertisement advertisementDeleted = this.advertisementService.findAdvertisementById(advertisementId);
        assertThat(advertisementDeleted).isNull();
    }

    @Test
    @Order(13)
    void shouldThrowExceptionWhenTryingToDeleteNullAdvertisement() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.advertisementService.deleteAdvertisement(null));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Entity must not be null!; nested exception is java.lang.IllegalArgumentException: Entity must not be null!");

    }

}
