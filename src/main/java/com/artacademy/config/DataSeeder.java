package com.artacademy.config;

import com.artacademy.entity.*;
import com.artacademy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        private final ArtWorksRepository artWorksRepository;
        private final ArtWorksCategoryRepository artWorksCategoryRepository;

        private final ArtMaterialsRepository artMaterialsRepository;
        private final ArtMaterialsCategoryRepository artMaterialsCategoryRepository;

        private final ArtClassesRepository artClassesRepository;
        private final ArtClassesCategoryRepository artClassesCategoryRepository;

        private final ArtExhibitionRepository artExhibitionRepository;
        private final ArtExhibitionCategoryRepository artExhibitionCategoryRepository;

        private final ArtGalleryRepository artGalleryRepository;
        private final ArtGalleryCategoryRepository artGalleryCategoryRepository;

        @Override
        public void run(String... args) throws Exception {
                log.info("--- Starting Application Data Seeding ---");

                // 1. Initialize Essential Roles (System Foundation)
                Map<String, Role> roles = seedRoles();

                // 2. Check if Admin exists. If yes, assume system is already initialized.
                if (userRepository.findByEmail("admin@artacademy.com").isPresent()) {
                        log.info("System already seeded. Skipping initialization.");
                        return;
                }

                // 3. Create Super Admin
                seedAdmin(roles.get("ROLE_ADMIN"));

                // 4. Onboard Sample Customers
                seedCustomers(roles.get("ROLE_CUSTOMER"));

                // 5. Seed Art Entities
                seedArtWorks();
                seedArtMaterials();
                seedArtClasses();
                seedArtExhibitions();
                seedArtGalleries();

                log.info("--- Data Seeding Completed Successfully ---");
        }

        private Map<String, Role> seedRoles() {
                log.info("[Phase 1] Seeding Roles...");
                return java.util.List.of("ROLE_CUSTOMER", "ROLE_ADMIN").stream()
                                .map(name -> roleRepository.findByName(name)
                                                .orElseGet(() -> roleRepository
                                                                .save(Role.builder().name(name).build())))
                                .collect(Collectors.toMap(Role::getName, role -> role));
        }

        private User seedAdmin(Role adminRole) {
                log.info("[Phase 2] Seeding Super Admin...");
                Set<User.RoleRef> roleRefs = new HashSet<>();
                roleRefs.add(User.RoleRef.builder()
                                .roleId(adminRole.getId())
                                .name(adminRole.getName())
                                .build());

                return userRepository.save(User.builder()
                                .firstName("Super")
                                .lastName("Admin")
                                .email("admin@artacademy.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("0000000000")
                                .roles(roleRefs)
                                .isEnabled(true)
                                .build());
        }

        private void seedCustomers(Role customerRole) {
                log.info("[Phase 3] Onboarding Customers...");
                Set<User.RoleRef> roleRefs = new HashSet<>();
                roleRefs.add(User.RoleRef.builder()
                                .roleId(customerRole.getId())
                                .name(customerRole.getName())
                                .build());

                userRepository.save(User.builder()
                                .firstName("Alice")
                                .lastName("Artist")
                                .email("alice@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231234")
                                .roles(roleRefs)
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Bob")
                                .lastName("Collector")
                                .email("bob@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231235")
                                .roles(roleRefs)
                                .isEnabled(true)
                                .build());
        }

        // Helper to create CategoryRef from category entity
        private ArtWorks.CategoryRef toCategoryRef(ArtWorksCategory cat) {
                return ArtWorks.CategoryRef.builder()
                                .categoryId(cat.getId())
                                .name(cat.getName())
                                .build();
        }

        private ArtMaterials.CategoryRef toMaterialsCategoryRef(ArtMaterialsCategory cat) {
                return ArtMaterials.CategoryRef.builder()
                                .categoryId(cat.getId())
                                .name(cat.getName())
                                .build();
        }

        private ArtClasses.CategoryRef toClassesCategoryRef(ArtClassesCategory cat) {
                return ArtClasses.CategoryRef.builder()
                                .categoryId(cat.getId())
                                .name(cat.getName())
                                .build();
        }

        private ArtExhibition.CategoryRef toExhibitionCategoryRef(ArtExhibitionCategory cat) {
                return ArtExhibition.CategoryRef.builder()
                                .categoryId(cat.getId())
                                .name(cat.getName())
                                .build();
        }

        private ArtGallery.CategoryRef toGalleryCategoryRef(ArtGalleryCategory cat) {
                return ArtGallery.CategoryRef.builder()
                                .categoryId(cat.getId())
                                .name(cat.getName())
                                .build();
        }

        private void seedArtWorks() {
                log.info("[Phase 4] Seeding Art Works...");
                if (artWorksRepository.count() > 0)
                        return;

                // Create categories
                ArtWorksCategory paintingCat = artWorksCategoryRepository
                                .save(ArtWorksCategory.builder().name("Paintings").build());
                ArtWorksCategory sculptureCat = artWorksCategoryRepository
                                .save(ArtWorksCategory.builder().name("Sculptures").build());
                ArtWorksCategory photographyCat = artWorksCategoryRepository
                                .save(ArtWorksCategory.builder().name("Photography").build());

                // Seed artworks with embedded category refs
                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Abstract Painting " + i)
                                        .description("A beautiful abstract painting #" + i)
                                        .basePrice(BigDecimal.valueOf(1500 + i * 250))
                                        .artistName("Maria Santos")
                                        .artMedium("Oil on Canvas")
                                        .size("24x36")
                                        .views(100 + i * 10)
                                        .likes(50 + i * 5)
                                        .category(toCategoryRef(paintingCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Painting+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Modern Sculpture " + i)
                                        .description("Contemporary bronze sculpture #" + i)
                                        .basePrice(BigDecimal.valueOf(3500 + i * 500))
                                        .artistName("David Chen")
                                        .artMedium("Bronze")
                                        .size("12x12x24")
                                        .views(80 + i * 10)
                                        .likes(40 + i * 5)
                                        .category(toCategoryRef(sculptureCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Sculpture+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artWorksRepository.save(ArtWorks.builder()
                                        .name("Urban Landscape " + i)
                                        .description("Fine art photography #" + i)
                                        .basePrice(BigDecimal.valueOf(800 + i * 150))
                                        .artistName("Elena Rodriguez")
                                        .artMedium("Digital Print on Canvas")
                                        .size("20x30")
                                        .views(150 + i * 20)
                                        .likes(75 + i * 10)
                                        .category(toCategoryRef(photographyCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Photography+" + i)
                                        .build());
                });
        }

        private void seedArtMaterials() {
                log.info("[Phase 5] Seeding Art Materials...");
                if (artMaterialsRepository.count() > 0)
                        return;

                ArtMaterialsCategory brushCat = artMaterialsCategoryRepository
                                .save(ArtMaterialsCategory.builder().name("Brushes").build());
                ArtMaterialsCategory paintCat = artMaterialsCategoryRepository
                                .save(ArtMaterialsCategory.builder().name("Paints").build());
                ArtMaterialsCategory canvasCat = artMaterialsCategoryRepository
                                .save(ArtMaterialsCategory.builder().name("Canvases").build());

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Professional Brush Set " + i)
                                        .description("Premium synthetic brushes for oil and acrylic painting")
                                        .basePrice(BigDecimal.valueOf(45 + i * 10))
                                        .discount(10)
                                        .stock(BigDecimal.valueOf(100))
                                        .category(toMaterialsCategoryRef(brushCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Brush+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Acrylic Paint Set " + i)
                                        .description("24-color professional grade acrylic paint set")
                                        .basePrice(BigDecimal.valueOf(65 + i * 15))
                                        .discount(5)
                                        .stock(BigDecimal.valueOf(75))
                                        .category(toMaterialsCategoryRef(paintCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Paint+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artMaterialsRepository.save(ArtMaterials.builder()
                                        .name("Stretched Canvas " + i)
                                        .description("Pre-stretched cotton canvas")
                                        .basePrice(BigDecimal.valueOf(55 + i * 10))
                                        .discount(0)
                                        .stock(BigDecimal.valueOf(50))
                                        .category(toMaterialsCategoryRef(canvasCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Canvas+" + i)
                                        .build());
                });
        }

        private void seedArtClasses() {
                log.info("[Phase 6] Seeding Art Classes...");
                if (artClassesRepository.count() > 0)
                        return;

                ArtClassesCategory workshopCat = artClassesCategoryRepository
                                .save(ArtClassesCategory.builder().name("Workshops").build());
                ArtClassesCategory onlineCat = artClassesCategoryRepository
                                .save(ArtClassesCategory.builder().name("Online Courses").build());
                ArtClassesCategory privateCat = artClassesCategoryRepository
                                .save(ArtClassesCategory.builder().name("Private Lessons").build());

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Watercolor Workshop " + i)
                                        .description("Intensive watercolor workshop covering fundamentals")
                                        .basePrice(BigDecimal.valueOf(150 + i * 25))
                                        .proficiency("Beginner")
                                        .category(toClassesCategoryRef(workshopCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Workshop+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Digital Art Fundamentals " + i)
                                        .description("Online course covering digital illustration")
                                        .basePrice(BigDecimal.valueOf(299 + i * 50))
                                        .proficiency("Intermediate")
                                        .category(toClassesCategoryRef(onlineCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Online+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artClassesRepository.save(ArtClasses.builder()
                                        .name("Portrait Sessions " + i)
                                        .description("Private lessons focused on portrait techniques")
                                        .basePrice(BigDecimal.valueOf(120 + i * 20))
                                        .proficiency("All Levels")
                                        .category(toClassesCategoryRef(privateCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Private+" + i)
                                        .build());
                });
        }

        private void seedArtExhibitions() {
                log.info("[Phase 7] Seeding Art Exhibitions...");
                if (artExhibitionRepository.count() > 0)
                        return;

                ArtExhibitionCategory modernCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder().name("Modern Art").build());
                ArtExhibitionCategory classicalCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder().name("Classical Art").build());
                ArtExhibitionCategory contemporaryCat = artExhibitionCategoryRepository
                                .save(ArtExhibitionCategory.builder().name("Contemporary").build());

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Future Vision " + i)
                                        .description("Exploring futuristic themes and abstract expressionism")
                                        .startDate(LocalDate.now().plusDays(i * 10))
                                        .endDate(LocalDate.now().plusDays(i * 10 + 30))
                                        .location("Gallery Hall A-" + i)
                                        .artistCount(12 + i)
                                        .artworksCount(45 + i * 5)
                                        .category(toExhibitionCategoryRef(modernCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Modern+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("Renaissance Masters " + i)
                                        .description("Timeless masterpieces of the Renaissance era")
                                        .startDate(LocalDate.now().plusDays(i * 15))
                                        .endDate(LocalDate.now().plusDays(i * 15 + 45))
                                        .location("Historic Wing " + i)
                                        .artistCount(8 + i)
                                        .artworksCount(30 + i * 3)
                                        .category(toExhibitionCategoryRef(classicalCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Classical+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artExhibitionRepository.save(ArtExhibition.builder()
                                        .name("New Voices " + i)
                                        .description("Emerging artists showcase contemporary works")
                                        .startDate(LocalDate.now().plusDays(i * 5))
                                        .endDate(LocalDate.now().plusDays(i * 5 + 21))
                                        .location("East Gallery " + i)
                                        .artistCount(20 + i * 2)
                                        .artworksCount(60 + i * 10)
                                        .category(toExhibitionCategoryRef(contemporaryCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Contemporary+" + i)
                                        .build());
                });
        }

        private void seedArtGalleries() {
                log.info("[Phase 8] Seeding Art Galleries...");
                if (artGalleryRepository.count() > 0)
                        return;

                ArtGalleryCategory downtownCat = artGalleryCategoryRepository
                                .save(ArtGalleryCategory.builder().name("Downtown").build());
                ArtGalleryCategory artsDistrictCat = artGalleryCategoryRepository
                                .save(ArtGalleryCategory.builder().name("Arts District").build());
                ArtGalleryCategory waterfrontCat = artGalleryCategoryRepository
                                .save(ArtGalleryCategory.builder().name("Waterfront").build());

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("The Grand Gallery " + i)
                                        .description("Premier art destination featuring world-class exhibitions")
                                        .category(toGalleryCategoryRef(downtownCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Downtown+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Studio Collective " + i)
                                        .description("Artist-run gallery showcasing emerging artists")
                                        .category(toGalleryCategoryRef(artsDistrictCat))
                                        .imageUrl("https://via.placeholder.com/300?text=ArtsDistrict+" + i)
                                        .build());
                });

                IntStream.rangeClosed(1, 2).forEach(i -> {
                        artGalleryRepository.save(ArtGallery.builder()
                                        .name("Harbor View Gallery " + i)
                                        .description("Scenic waterfront gallery specializing in marine art")
                                        .category(toGalleryCategoryRef(waterfrontCat))
                                        .imageUrl("https://via.placeholder.com/300?text=Waterfront+" + i)
                                        .build());
                });
        }
}
