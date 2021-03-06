package org.springframework.samples.flatbook.unit.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.flatbook.configuration.SecurityConfiguration;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.model.pojos.GeocodeResult;
import org.springframework.samples.flatbook.model.pojos.Geometry;
import org.springframework.samples.flatbook.model.pojos.Location;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.RequestService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.samples.flatbook.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.FlatController;
import org.springframework.samples.flatbook.web.converters.MultipartToDBImageConverter;
import org.springframework.samples.flatbook.web.validators.FlatValidator;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FlatController.class,
    includeFilters = {@ComponentScan.Filter(value = MultipartToDBImageConverter.class, type = FilterType.ASSIGNABLE_TYPE),
        @ComponentScan.Filter(value = FlatValidator.class, type = FilterType.ASSIGNABLE_TYPE),
        @ComponentScan.Filter(value = ReviewUtils.class, type = FilterType.ASSIGNABLE_TYPE)},
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration= SecurityConfiguration.class)
class FlatControllerTests {

    private static final Integer TEST_FLAT_ID = 1;
    private static final Integer TEST_FLAT_ID2 = 2;
    private static final Integer TEST_FLAT_ID3 = 3;
    private static final String TEST_PERSON_USERNAME = "spring";
    private static final Integer TEST_IMAGE_ID = 1;
    private static final String TEST_CITY_FLAT = "Seville";
    private static final String TEST_CITY_FLAT_NOT_EXISTS = "not";
    private static final String TEST_ADDRESS_NOT_EXISTS = "notexists";
    private static final String TEST_ADDRESS_UPDATE = "Calle Luis Montoto";
    private static final String TEST_COUNTRY_FLAT = "Spain";
    private static final String TEST_POSTAL_CODE_FLAT = "41010";
    private static final double LATITUDE = 37.3822261;
	private static final double LONGITUDE = -6.0123468;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlatService flatService;

    @MockBean
    private FlatReviewService flatReviewService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private DBImageService dbImageService;

    @MockBean
    private PersonService personService;

    @MockBean
    private HostService hostService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private GeocodeAPIService geocodeAPIService;

    @BeforeEach
    void setup() {
        Set<DBImage> images = new HashSet<>();

        Address address = new Address();
        address.setLocation("Avenida de la República Argentina");
        address.setPostalCode("41011");
        address.setCity("Sevilla");
        address.setCountry("Spain");
        address.setLatitude(LATITUDE);
        address.setLongitude(LONGITUDE);

        GeocodeResponse response = new GeocodeResponse();
        List<GeocodeResult> resultList = new ArrayList<>();
        GeocodeResult result = new GeocodeResult();
        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLat(address.getLatitude());
        location.setLng(address.getLongitude());
        geometry.setLocation(location);
        result.setGeometry(geometry);
        resultList.add(result);
        response.setResults(resultList);
        response.setStatus("OK");

        GeocodeResponse responseZeroResults = new GeocodeResponse();
        responseZeroResults.setStatus("ZERO_RESULTS");

        GeocodeResponse responseError = new GeocodeResponse();
        responseError.setStatus("ERROR");

        DBImage image = new DBImage();
        image.setId(TEST_IMAGE_ID);
        images.add(image);
        images.add(new DBImage());
        images.add(new DBImage());
        images.add(new DBImage());
        images.add(new DBImage());
        images.add(new DBImage());
        images.add(new DBImage());

        Set<FlatReview> fr = new HashSet<>();
        Set<Tenant> tnnts = new HashSet<>();
        Flat flat = new Flat();
        flat.setId(TEST_FLAT_ID);
        flat.setDescription("this is a sample description with more than 30 chars");
        flat.setSquareMeters(90);
        flat.setNumberBaths(2);
        flat.setNumberRooms(2);
        flat.setAvailableServices("Wifi and cable TV");
        flat.setAddress(address);
        flat.setImages(images);
        flat.setFlatReviews(fr);
        flat.setTenants(tnnts);

        Set<DBImage> fourImages = new HashSet<>();
        fourImages.add(image);
        fourImages.add(new DBImage());
        fourImages.add(new DBImage());
        fourImages.add(new DBImage());

        Flat flat4Images = new Flat();
        flat4Images.setId(TEST_FLAT_ID2);
        flat4Images.setDescription("this is a sample description with more than 30 chars");
        flat4Images.setSquareMeters(90);
        flat4Images.setNumberBaths(2);
        flat4Images.setNumberRooms(2);
        flat4Images.setAvailableServices("Wifi and cable TV");
        flat4Images.setAddress(address);
        flat4Images.setImages(fourImages);
        flat4Images.setFlatReviews(fr);

        Set<DBImage> sixImages = new HashSet<>();
        sixImages.add(image);
        sixImages.add(new DBImage());
        sixImages.add(new DBImage());
        sixImages.add(new DBImage());
        sixImages.add(new DBImage());
        sixImages.add(new DBImage());

        Flat flat6Images = new Flat();
        flat6Images.setId(TEST_FLAT_ID3);
        flat6Images.setDescription("this is a sample description with more than 30 chars");
        flat6Images.setSquareMeters(90);
        flat6Images.setNumberBaths(2);
        flat6Images.setNumberRooms(2);
        flat6Images.setAvailableServices("Wifi and cable TV");
        flat6Images.setAddress(address);
        flat6Images.setImages(sixImages);
        flat6Images.setFlatReviews(fr);

        Set<Flat> flatsOfHost = new HashSet<>();
        flatsOfHost.add(flat);

        Host host = new Host();
        host.setUsername(TEST_PERSON_USERNAME);
        host.setEnabled(true);

        given(this.personService.findUserById(TEST_PERSON_USERNAME)).willReturn(host);
        given(this.flatService.findFlatById(TEST_FLAT_ID)).willReturn(flat);
        given(this.flatService.findFlatById(TEST_FLAT_ID2)).willReturn(flat4Images);
        given(this.flatService.findFlatById(TEST_FLAT_ID3)).willReturn(flat6Images);
        given(this.flatService.findFlatByIdWithFullData(TEST_FLAT_ID)).willReturn(flat);
        given(this.flatService.findFlatByHostUsername(TEST_PERSON_USERNAME)).willReturn(flatsOfHost);
        given(this.dbImageService.getImageById(TEST_IMAGE_ID)).willReturn(image);
        given(this.dbImageService.getImagesByFlatId(TEST_FLAT_ID)).willReturn(images);
        given(this.dbImageService.getImagesByFlatId(TEST_FLAT_ID2)).willReturn(fourImages);
        given(this.dbImageService.getImagesByFlatId(TEST_FLAT_ID3)).willReturn(sixImages);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID)).willReturn(host);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID2)).willReturn(host);
        given(this.hostService.findHostByFlatId(TEST_FLAT_ID3)).willReturn(host);
        given(this.advertisementService.isAdvertisementWithFlatId(TEST_FLAT_ID)).willReturn(false);
        given(this.authoritiesService.findAuthorityById(TEST_PERSON_USERNAME)).willReturn(AuthoritiesType.HOST);
        willAnswer(invocation -> {
            DBImage arg0 = invocation.getArgument(0);
            flat.deleteImage(arg0);
            return flat;
        }).given(this.dbImageService).deleteImage(image);
        try {
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(address.getLocation() + ", " + address.getCity())).willReturn(response);
            BDDMockito.given(this.geocodeAPIService.getGeocodeData(TEST_ADDRESS_UPDATE + ", " + address.getCity())).willReturn(response);
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(TEST_ADDRESS_NOT_EXISTS + ", " + TEST_CITY_FLAT_NOT_EXISTS)).willReturn(responseZeroResults);
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(TEST_ADDRESS_NOT_EXISTS + ", " + address.getCity())).willReturn(responseError);
			BDDMockito.given(this.geocodeAPIService.getGeocodeData(TEST_CITY_FLAT + ", " + TEST_COUNTRY_FLAT + " " + TEST_POSTAL_CODE_FLAT)).willReturn(response);
        } catch (UnsupportedEncodingException e) {
		}
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/flats/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("flats/createOrUpdateFlatForm"))
            .andExpect(model().attributeExists("flat"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessCreationFormSuccess() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

        mockMvc.perform(multipart("/flats/new")
            .file(file1)
            .file(file2)
            .file(file3)
            .file(file4)
            .file(file5)
            .file(file6)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", "Avenida de la República Argentina")
            .param("address.postalCode", "41011")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
            .andExpect(status().is3xxRedirection());
        then(this.flatService).should().saveFlat(Mockito.isA(Flat.class));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

        mockMvc.perform(multipart("/flats/new")
            .file(file1)
            .file(file2)
            .file(file3)
            .file(file4)
            .file(file5)
            .file(file6)
            .with(csrf())
            .param("description", "sample description w 29 chars")
            .param("squareMeters", "90")
            .param("numberBaths", "0")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", "Avenida de la República Argentina")
            .param("address.postalCode", "41011")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("flat"))
            .andExpect(model().attributeHasFieldErrors("flat", "description"))
            .andExpect(model().attributeHasFieldErrors("flat", "numberRooms"))
            .andExpect(model().attributeHasFieldErrors("flat", "numberBaths"))
            .andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessCreationFormHasNotEnoughImages() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());

        mockMvc.perform(multipart("/flats/new")
            .file(file1)
            .file(file2)
            .file(file3)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", "Avenida de la República Argentina")
            .param("address.postalCode", "41011")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("flat"))
            .andExpect(model().attributeHasFieldErrors("flat", "images"))
            .andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessCreationFormZeroResults() throws Exception {
    	MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

        mockMvc.perform(multipart("/flats/new")
            .file(file1)
            .file(file2)
            .file(file3)
            .file(file4)
            .file(file5)
            .file(file6)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", TEST_ADDRESS_NOT_EXISTS)
            .param("address.postalCode", "41011")
            .param("address.city", TEST_CITY_FLAT_NOT_EXISTS)
            .param("address.country", "Spain"))
        	.andExpect(status().isOk())
        	.andExpect(model().attributeHasErrors("flat"))
            .andExpect(model().attributeHasFieldErrors("flat", "address.location"))
        	.andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessCreationFormErrorAddress() throws Exception {
    	MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.png", "image/png", "image3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "image4.png", "image/png", "image4".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "image5.png", "image/png", "image5".getBytes());
        MockMultipartFile file6 = new MockMultipartFile("images", "image6.png", "image/png", "image6".getBytes());

        mockMvc.perform(multipart("/flats/new")
            .file(file1)
            .file(file2)
            .file(file3)
            .file(file4)
            .file(file5)
            .file(file6)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", TEST_ADDRESS_NOT_EXISTS)
            .param("address.postalCode", "41011")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
        	.andExpect(status().isOk())
        	.andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testInitUpdateForm() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/edit", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("flat"))
            .andExpect(model().attribute("flat", hasProperty("description", is("this is a sample description with more than 30 chars"))))
            .andExpect(model().attribute("flat", hasProperty("squareMeters", is(90))))
            .andExpect(model().attribute("flat", hasProperty("numberRooms", is(2))))
            .andExpect(model().attribute("flat", hasProperty("numberBaths", is(2))))
            .andExpect(model().attribute("flat", hasProperty("availableServices", is("Wifi and cable TV"))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("location", is("Avenida de la República Argentina")))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("postalCode", is("41011")))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("city", is("Sevilla")))))
            .andExpect(model().attribute("flat", hasProperty("address", hasProperty("country", is("Spain")))))
            .andExpect(model().attributeExists("images"))
            .andExpect(model().attribute("images", hasSize(7)))
            .andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

    @WithMockUser(username = "spring-wrong", authorities = {"HOST"})
    @Test
    void testInitUpdateFormThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/edit", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessUpdateFormSuccess() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());

        mockMvc.perform(multipart("/flats/{flatId}/edit", TEST_FLAT_ID)
            .file(file1)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", TEST_ADDRESS_UPDATE)
            .param("address.postalCode", "41003")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/flats/{flatId}"));
        then(this.flatService).should().saveFlat(Mockito.isA(Flat.class));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessUpdateFormHasNotMininmumImages() throws Exception {
    	MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());
        mockMvc.perform(multipart("/flats/{flatId}/edit", TEST_FLAT_ID2)
        	.file(file1)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", "Calle Luis Montoto")
            .param("address.postalCode", "41003")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
        	.andExpect(status().isOk())
        	.andExpect(model().attributeHasErrors("flat"))
        	.andExpect(model().attributeHasFieldErrors("flat", "images"))
        	.andExpect(view().name("flats/createOrUpdateFlatForm"));
    }


    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessUpdateFormWithErrors() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "", "application/octet-stream", new byte[]{});

        mockMvc.perform(multipart("/flats/{flatId}/edit", TEST_FLAT_ID)
            .file(file1)
            .with(csrf())
            .param("description", "sample description w 29 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("address.location", "Calle Luis Montoto")
            .param("address.postalCode", "41003")
            .param("address.city", "Sevilla"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("flat"))
            .andExpect(model().attributeHasFieldErrors("flat", "description"))
            .andExpect(model().attributeHasFieldErrors("flat", "availableServices"))
            .andExpect(model().attributeHasFieldErrors("flat", "address.country"))
            .andExpect(view().name("flats/createOrUpdateFlatForm"));
    }

    @WithMockUser(username = "spring-wrong", authorities = {"HOST"})
    @Test
    void testProcessUpdateFormThrowExceptionWithWrongHost() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.png", "image/png", "image1".getBytes());

        mockMvc.perform(multipart("/flats/{flatId}/edit", TEST_FLAT_ID)
            .file(file1)
            .with(csrf())
            .param("description", "this is a sample description with more than 30 chars")
            .param("squareMeters", "90")
            .param("numberRooms", "2")
            .param("numberBaths", "2")
            .param("availableServices", "Wifi and cable TV")
            .param("address.location", "Calle Luis Montoto")
            .param("address.postalCode", "41003")
            .param("address.city", "Sevilla")
            .param("address.country", "Spain"))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessDeleteImage() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/images/{imageId}/delete", TEST_FLAT_ID, TEST_IMAGE_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/flats/{flatId}/edit"));
        then(this.dbImageService).should().deleteImage(Mockito.isA(DBImage.class));
    }

    @WithMockUser(username = "spring-wrong", authorities = {"HOST"})
    @Test
    void testProcessDeleteImageThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/images/{imageId}/delete", TEST_FLAT_ID, TEST_IMAGE_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessDeleteImageThrowException6Images() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/images/{imageId}/delete", TEST_FLAT_ID3, TEST_IMAGE_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testShowFlat() throws Exception {
        mockMvc.perform(get("/flats/{flatId}", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("flat"))
            .andExpect(model().attributeExists("images"))
            .andExpect(model().attributeExists("host"))
            .andExpect(view().name("flats/flatDetails"));

    }

    @WithMockUser(username = "spring-wrong-tenant", authorities = {"TENANT"})
    @Test
    void testShowFlatExceptionNotAllowed() throws Exception {
        mockMvc.perform(get("/flats/{flatId}", TEST_FLAT_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("exception"));
    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testShowFlatsOfHost() throws Exception {
        mockMvc.perform(get("/flats/list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("flats"))
            .andExpect(model().attributeExists("advIds"))
            .andExpect(view().name("flats/flatsOfHost"));

    }

    @WithMockUser(username = "spring", authorities = {"HOST"})
    @Test
    void testProcessFlatRemovalSucess() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/delete", TEST_FLAT_ID))
            .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(username = "spring-wrong", authorities = {"HOST"})
    @Test
    void testProcessFlatRemovalThrowExceptionWithWrongHost() throws Exception {
        mockMvc.perform(get("/flats/{flatId}/delete", TEST_FLAT_ID))
        	.andExpect(status().is2xxSuccessful())
        	.andExpect(view().name("exception"));
    }
}
