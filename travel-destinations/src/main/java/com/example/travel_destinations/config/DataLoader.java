
package com.example.travel_destinations.config;

import com.example.travel_destinations.entity.*;
import com.example.travel_destinations.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public void run(String... args) {

        // додаємо travellers
        Traveler oleg = new Traveler("Олег Петров", "oleg@politech.com");
        oleg.setPhone("+380501112233");
        oleg.setTravelStyle(Traveler.TravelStyle.SOLO);
        oleg.setRegistrationDate(LocalDate.now().minusDays(30));
        travelerRepository.save(oleg);

        Traveler ira = new Traveler("Іра Коваленко", "ira@politech.com");
        ira.setPhone("+380671234567");
        ira.setTravelStyle(Traveler.TravelStyle.COUPLE);
        ira.setRegistrationDate(LocalDate.now().minusDays(20));
        travelerRepository.save(ira);

        Traveler taras = new Traveler("Тарас Бульба", "taras@politech.com");
        taras.setPhone("+380931234567");
        taras.setTravelStyle(Traveler.TravelStyle.FAMILY);
        taras.setRegistrationDate(LocalDate.now().minusDays(15));
        travelerRepository.save(taras);

        Traveler katya = new Traveler("Катя Лісова", "katya@politech.com");
        katya.setTravelStyle(Traveler.TravelStyle.GROUP);
        katya.setRegistrationDate(LocalDate.now().minusDays(10));
        travelerRepository.save(katya);

        System.out.println("Створено 4 travellers");

        // додаємо destinations

        Destination lviv = new Destination("Україна", "Львів", Destination.DestinationCategory.CITY);
        lviv.setRating(4.8);
        lviv.setDescription("Місто кави, шоколаду і трамваю, що завжди приїжджає на 5 хв пізніше.");
        lviv.setBestSeason(Destination.Season.SUMMER);
        destinationRepository.save(lviv);

        Destination odessa = new Destination("Україна", "Одеса", Destination.DestinationCategory.BEACH);
        odessa.setRating(4.6);
        odessa.setDescription("Море, жарти й тьотя Соня з Привозу.");
        odessa.setBestSeason(Destination.Season.SUMMER);
        destinationRepository.save(odessa);

        Destination karpaty = new Destination("Україна", "Карпати", Destination.DestinationCategory.MOUNTAINS);
        karpaty.setRating(4.9);
        karpaty.setDescription("Гори, трембіти та шанс з'їсти більше баношу, ніж планував.");
        karpaty.setBestSeason(Destination.Season.AUTUMN);
        destinationRepository.save(karpaty);
        Destination paris = new Destination("Франція", "Париж", Destination.DestinationCategory.CITY);
        paris.setRating(4.7);
        paris.setDescription("Місто кохання і круасанів. Мінус: черга в Луврі така, що Мона Ліза вже встигла б переморгнути.");
        paris.setBestSeason(Destination.Season.SPRING);
        destinationRepository.save(paris);

        Destination barcelona = new Destination("Іспанія", "Барселона", Destination.DestinationCategory.CITY);
        barcelona.setRating(4.8);
        barcelona.setDescription("Гауді, пляжі й шанс згоріти на сонці ще в квітні.");
        barcelona.setBestSeason(Destination.Season.SPRING);
        destinationRepository.save(barcelona);

        Destination alps = new Destination("Швейцарія", "Альпи", Destination.DestinationCategory.MOUNTAINS);
        alps.setRating(4.9);
        alps.setDescription("Тут навіть корови виглядають багатшими за тебе.");
        alps.setBestSeason(Destination.Season.WINTER);
        destinationRepository.save(alps);

        Destination tokyo = new Destination("Японія", "Токіо", Destination.DestinationCategory.CITY);
        tokyo.setRating(4.9);
        tokyo.setDescription("Неонові вивіски, суші, і туалети розумніші за мене.");
        tokyo.setBestSeason(Destination.Season.SPRING);
        destinationRepository.save(tokyo);

        Destination maldives = new Destination("Мальдіви", "Мале", Destination.DestinationCategory.BEACH);
        maldives.setRating(5.0);
        maldives.setDescription("Рай на землі. Мінус: після повернення звичайний пляж виглядає як калюжа.");
        maldives.setBestSeason(Destination.Season.WINTER);
        destinationRepository.save(maldives);

        System.out.println("Створено 8 destinations");

        //встановлюємо favs destination
        oleg.setFavoriteDestinationId(karpaty.getDestinationId());
        ira.setFavoriteDestinationId(maldives.getDestinationId());
        taras.setFavoriteDestinationId(lviv.getDestinationId());
        katya.setFavoriteDestinationId(barcelona.getDestinationId());

        travelerRepository.save(oleg);
        travelerRepository.save(ira);
        travelerRepository.save(taras);
        travelerRepository.save(katya);

        System.out.println("Встановлено улюблені напрямки");

        // додаємо trips
        Trip carpathianHike = new Trip("Карпатський хайкінг",
                LocalDate.of(2024, 8, 1),
                LocalDate.of(2024, 8, 10),
                new BigDecimal("1000.00"));
        carpathianHike.setTraveler(oleg);
        carpathianHike.setStatus(Trip.TripStatus.COMPLETED);
        carpathianHike.setDescription("Піші походи Карпатами з ночівлею у горах");
        tripRepository.save(carpathianHike);
        carpathianHike.getDestinations().add(karpaty); // Додаємо destination
        tripRepository.save(carpathianHike); // Зберігаємо З destination

        Trip romanticMaldives = new Trip("Медовий місяць на Мальдівах",
                LocalDate.of(2024, 7, 15),
                LocalDate.of(2024, 7, 25),
                new BigDecimal("5000.00"));
        romanticMaldives.setTraveler(ira);
        romanticMaldives.setStatus(Trip.TripStatus.COMPLETED);
        romanticMaldives.setDescription("Романтична подорож у тропічний рай");
        tripRepository.save(romanticMaldives);
        romanticMaldives.getDestinations().add(maldives);
        tripRepository.save(romanticMaldives);

        Trip lvivFamilyTrip = new Trip("Сімейна кава у Львові",
                LocalDate.of(2024, 12, 24),
                LocalDate.of(2024, 12, 27),
                new BigDecimal("700.00"));
        lvivFamilyTrip.setTraveler(taras);
        lvivFamilyTrip.setStatus(Trip.TripStatus.PLANNED);
        lvivFamilyTrip.setDescription("Новорічні свята у Львові з родиною");
        tripRepository.save(lvivFamilyTrip);
        lvivFamilyTrip.getDestinations().add(lviv);
        tripRepository.save(lvivFamilyTrip);

        Trip tokyoAdventure = new Trip("Ніч у Токіо без сну",
                LocalDate.of(2024, 10, 3),
                LocalDate.of(2024, 10, 12),
                new BigDecimal("2500.00"));
        tokyoAdventure.setTraveler(katya);
        tokyoAdventure.setStatus(Trip.TripStatus.PLANNED);
        tokyoAdventure.setDescription("Пригоди у найбільшому мегаполісі світу");
        tripRepository.save(tokyoAdventure);
        tokyoAdventure.getDestinations().add(tokyo);
        tripRepository.save(tokyoAdventure);

        Trip euroTrip = new Trip("Європейський тур",
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 9, 15),
                new BigDecimal("5000.00"));
        euroTrip.setTraveler(oleg);
        euroTrip.setStatus(Trip.TripStatus.PLANNED);
        euroTrip.setDescription("Подорож по найкрасивіших містах Європи");
        tripRepository.save(euroTrip);
        euroTrip.getDestinations().add(paris); // Додаємо декілька destinations
        euroTrip.getDestinations().add(barcelona);
        tripRepository.save(euroTrip);

        Trip odessaBeach = new Trip("Літній пляжний відпочинок",
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2024, 7, 14),
                new BigDecimal("1500.00"));
        odessaBeach.setTraveler(ira);
        odessaBeach.setStatus(Trip.TripStatus.IN_PROGRESS);
        odessaBeach.setDescription("Морське узбережжя та одеський гумор");
        tripRepository.save(odessaBeach);
        odessaBeach.getDestinations().add(odessa);
        tripRepository.save(odessaBeach);

        System.out.println("Створено 6 trips");


        System.out.println("\n" +"Тестові дані завантажено!");
        System.out.println("=".repeat(60));
        System.out.println("Статистика бази даних:");
        System.out.println("Мандрівників: " + travelerRepository.count());
        System.out.println("Напрямків: " + destinationRepository.count());
        System.out.println("Подорожей: " + tripRepository.count());
    }


}