package org.cinema;

import org.cinema.Service.ICinemaInitService;
import org.cinema.entities.Film;
import org.cinema.entities.Salle;
import org.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {
    @Autowired
    private ICinemaInitService CinemaInitService;
    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;
   // @Autowired
    //private CinemaRepository cinemaRepository;*/
    public static void main(String[] args){
        SpringApplication.run(CinemaApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        repositoryRestConfiguration.exposeIdsFor(Film.class, Salle.class, Ticket.class);
        CinemaInitService.initVilles();CinemaInitService.initCinemas();
        CinemaInitService.initSalles();CinemaInitService.initPlaces();
        CinemaInitService.initSeances();CinemaInitService.initCategories();
        CinemaInitService.initFilms();CinemaInitService.initProjections();
        CinemaInitService.initTickets();
    }

        /*Stream.of("MegaRama","IMAX","FOUNOUN","CHAHRAZAD","DAOULIZ").forEach(nameCinema->{
            cinemaRepository.save(new Cinema(null,nameCinema,Math.random(),Math.random(),Math.random(),1+new Random().nextInt(9),null,null));
                });
        cinemaRepository.findAll().forEach(cinema -> {
            System.out.println(cinema.getName());
        });*/

}
