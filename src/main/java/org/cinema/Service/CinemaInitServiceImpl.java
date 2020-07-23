package org.cinema.Service;


import org.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.cinema.repositories.*;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
    @Autowired private VilleRepository villeRepository;
    @Autowired private CinemaRepository cinemaRepository;
    @Autowired private SalleRepository salleRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private SeanceRepository seanceRepository;
    @Autowired private FilmRepository filmRepository;
    @Autowired private ProjectionRepository projectionRepository;
    @Autowired private CategorieRepository categorieRepository;
    @Autowired private TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
            Ville ville=new Ville();
            ville.setName(nameVille);
            villeRepository.save(ville);
        });

    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(ville->{
            Stream.of("MegaRama","IMAX","FOUNOUN","CHAHRAZAD","DAOULIZ").forEach(nameCinema->{
                Cinema cinema=new Cinema();
                cinema.setName(nameCinema);
                cinema.setVille(ville);
                cinema.setNbr_salles(2+(int)(Math.random()*7));
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i=0;i<cinema.getNbr_salles();i++){
                Salle salle=new Salle();
                salle.setName("Salle"+(i+1));
                salle.setCinema(cinema);
                salle.setNbr_places(15+(int)(Math.random()*20));
                salleRepository.save(salle);
            }
        });

    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i=0;i<salle.getNbr_places();i++){
                Place place=new Place();
                place.setNumero(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });

    }

    @Override
    public void initSeances() {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Stream.of("12:00","18:00","21:00").forEach(s->{
            Seance seance=new Seance();
            try {
                seance.setHeure_debut(dateFormat.parse(s));
                seanceRepository.save(seance);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Histoire","Actions","Horror","Fiction","Drama").forEach(namecategorie->{
            Categorie categorie=new Categorie();
            categorie.setName(namecategorie);
            categorieRepository.save(categorie);
        });

    }

    @Override
    public void initFilms() {
        double[] durees=new double[] {1,1.5,2,2.5,3};
        List<Categorie> categories=categorieRepository.findAll();
        Stream.of("Casablanca","Leonardo","Malcom","Spies").forEach(nameFilm->{
            Film film=new Film();
            film.setTitre(nameFilm);
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            film.setPhoto(nameFilm.replaceAll(" ","")+".jpg");
            filmRepository.save(film);
        });

    }

    @Override
    public void initProjections() {
        double[] prices=new double[] {30,50,60,70,90,100};
        List<Film>films=filmRepository.findAll();
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    int index=new Random().nextInt(films.size());
                    Film film =films.get(index);
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection=new Projection();
                            projection.setDate_projection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);

                        });
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket=new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(projection.getPrix());
                ticket.setProjection(projection);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });


    }
}
