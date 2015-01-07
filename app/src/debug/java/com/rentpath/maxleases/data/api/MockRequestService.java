package com.rentpath.maxleases.data.api;

import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminderSession;
import com.gf.movie.reminder.data.model.Trailer;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Query;

@Singleton
public class MockRequestService implements RequestService {

    @Inject
    MockRequestService() {
    }

    @Override
    public void login(@Body HashMap creds, Callback<MovieReminderSession> cb) {
        MovieReminderSession session = new MovieReminderSession("test_token", ((String) creds.get("email")).substring(0, ((String) creds.get("email")).indexOf("@")),
                (String) creds.get("email"));
        cb.success(session, getMock200Response());
    }

    @Override
    public void register(@Body HashMap creds, Callback<MovieReminderSession> cb) {
        MovieReminderSession session = new MovieReminderSession("test_token", ((String) creds.get("email")).substring(0, ((String) creds.get("email")).indexOf("@")),
                (String) creds.get("email"), (String) creds.get("firstname"), (String) creds.get("lastname"));
        cb.success(session, getMock200Response());
    }

    @Override
    public void search(@Query("part") String query, Callback<ArrayList<Trailer>> cb) {

    }

    @Override
    public void getMovieTrailers(@Query("key") String apiKey, Callback<ArrayList<Movie>> cb) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        movies.add(new Movie("The Interview",
                "Dave Skylark (James Franco) and his producer Aaron " +
                        "Rapoport (Seth Rogen) are the team behind the popular tabloid-TV show \"Skylark Tonight.\" " +
                        "After learning that North Korea's Kim Jong Un (Randall Park) is a huge fan of the show, they " +
                        "successfully set up an interview with him, hoping to legitimize themselves as actual journalists. " +
                        "However, as Dave and Aaron prepare for their journey to Pyongyang, the CIA steps in, recruits them, " +
                        "and assigns them an incredible mission: Assassinate the dictator.",
                "http://upload.wikimedia.org/wikipedia/en/2/27/The_Interview_2014_poster.jpg",
                "https://www.youtube.com/watch?v=XcOVCkMpPGE",
                new Date(new Date().getTime() - 60000),
                "Seth Rogen, Evan Goldberg",
                "James Franco, Seth Rogen, Randall Park "));
        movies.add(new Movie("The Hobbit: The Battle of the Five Armies",
                "Bilbo and Company are forced to engage in a war against " +
                        "an array of combatants and keep the Lonely Mountain from falling into the hands of a rising darkness.",
                "http://vignette1.wikia.nocookie.net/lotr/images/f/fe/TBOT5A_Theatrical_Poster.jpg/revision/latest?cb=20141022210753",
                "https://www.youtube.com/watch?v=iVAgTiBrrDA",
                new Date(new Date().getTime() + (60000 * 2)),
                "Peter Jackson",
                "Ian McKellen, Martin Freeman, Richard Armitage, Cate Blanchett"));
        movies.add(new Movie("Night at the Museum: Secret of the Tomb",
                "Larry spans the globe, uniting favorite and new characters while embarking on an epic quest to save the " +
                        "magic before it is gone forever.",
                "http://ia.media-imdb.com/images/M/MV5BMjI1MzM2ODEyMV5BMl5BanBnXkFtZTgwNTIzODAwMzE@._V1_SX640_SY720_.jpg",
                "https://www.youtube.com/watch?v=Hr1fFMp0MqU",
                new Date(new Date().getTime() + (60000 * 3)),
                "Shawn Levy",
                "Ben Stiller, Robin Williams, Owen Wilson, Dick Van Dyke"));
        movies.add(new Movie("The Hunger Games: Mockingjay - Part 1",
                "When Katniss destroys the games, she goes to District 13 after District 12 is destroyed. She meets President " +
                        "Coin who convinces her to be the symbol of rebellion, while trying to save Peeta from the Capitol.",
                "http://imageserver.moviepilot.com/hungergamesmockingjay-the-hunger-games-mockingjay-part-1-hits-300-million.jpeg?width=720&height=1110",
                "https://www.youtube.com/watch?v=3PkkHsuMrho",
                new Date(new Date().getTime() + (60000 * 4)),
                "Francis Lawrence",
                "Jennifer Lawrence, Josh Hutcherson, Liam Hemsworth, Woody Harrelson"));
        movies.add(new Movie("Exodus: Gods and Kings",
                "The defiant leader Moses rises up against the Egyptian Pharaoh Ramses, setting 600,000 slaves on a monumental " +
                        "journey of escape from Egypt and its terrifying cycle of deadly plagues.",
                "https://s.yimg.com/cd/resizer/FIT_TO_WIDTH-w1383/10c4408ed1ac4b3d825a6ade7b9607a96ed906be.jpg",
                "https://www.youtube.com/watch?v=t-8YsulfxVI",
                new Date(new Date().getTime() + (60000 * 5)),
                "Ridley Scott",
                "Christian Bale, Joel Edgerton, Ben Kingsley, Sigourney Weaver"));
        movies.add(new Movie("Interstellar",
                "A team of explorers travel through a wormhole in an attempt to ensure humanity's survival.",
                "http://ia.media-imdb.com/images/M/MV5BMjIxNTU4MzY4MF5BMl5BanBnXkFtZTgwMzM4ODI3MjE@._V1_SX640_SY720_.jpg",
                "https://www.youtube.com/watch?v=0vxOhd4qlnA",
                new Date(new Date().getTime() + (60000 * 6)),
                "Christopher Nolan",
                "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Mackenzie Foy"));

        cb.success(movies, getMock200Response());
    }

    @Override
    public void getGameTrailers(@Query("key") String apiKey, Callback<ArrayList<Game>> cb) {
        ArrayList<Game> games = new ArrayList<Game>();
        games.add(new Game("Grand Theft Auto V",
                "Rockstar Games' critically acclaimed open world comes to a new generation \n\nEnter the lives of three " +
                        "very different criminals, Michael, Franklin and Trevor, as they risk everything in a series of daring " +
                        "and dangerous heists that could set them up for life. Explore the stunning world of Los Santos and Blaine " +
                        "County in the ultimate Grand Theft Auto V experience, featuring a range of technical upgrades and " +
                        "enhancements for new and returning players.",
                "http://upload.wikimedia.org/wikipedia/en/a/a5/Grand_Theft_Auto_V.png",
                "https://www.youtube.com/watch?v=hBvMSP7cI-Q",
                new Date(new Date().getTime() - 60000),
                Game.Console.ALL,
                "Rockstar Games"));
        games.add(new Game("Destiny",
                "Everything changed with the arrival of the Traveler. It sparked a Golden Age when our civilization spanned our solar " +
                        "system, but it didn't last. Something hit us, knocked us down. The survivors built a city beneath the Traveler, " +
                        "and have begun to explore our old worlds, only to find them filled with deadly foes. You are a Guardian of the last" +
                        " safe city on Earth, able to wield incredible power. Defend the City. Defeat our enemies. Reclaim all that we have lost." +
                        " Be brave.",
                "http://www.sggaminginfo.com/wp-content/uploads/2013/09/Destiny-PS4-Pre-Order-Inlay-2D-UK_1380551641.jpg",
                "https://www.youtube.com/watch?v=iMGE3B2wsS8",
                new Date(new Date().getTime() + (60000 * 2)),
                Game.Console.ALL,
                "Activision/Bungie"));
        games.add(new Game("Far Cry 4",
                "Hidden in the towering Himalayas lies Kyrat, a country steeped in tradition and violence. You are Ajay Ghale. " +
                        "Traveling to Kyrat to fulfill your mother's dying wish, you find yourself caught up in a civil war to " +
                        "overthrow the oppressive regime of dictator Pagan Min. Explore and navigate this vast open world, where " +
                        "danger and unpredictability lurk around every corner. Here, every decision counts, and every second is a " +
                        "story. Welcome to Kyrat. SEQUEL TO THE #1 RATED SHOOTER OF 2012*",
                "http://tech4gamers.com/wp-content/uploads/2014/05/far-cry-4.jpg",
                "https://www.youtube.com/watch?v=e9al_k8e93I",
                new Date(new Date().getTime() + (60000 * 3)),
                Game.Console.ALL,
                "UbiSoft"));
        games.add(new Game("Dragon Age Inquisition",
                "The epic role-playing series from BioWare takes a thrilling leap forward with the power of Frostbite 3. Beautiful vistas " +
                        "and incredible new possibilities await you. Ready yourself for Dragon Age: Inquisition.",
                "http://upload.wikimedia.org/wikipedia/en/c/ce/Dragon_Age_Inquisition_BoxArt.jpg",
                "https://www.youtube.com/watch?v=7UpfW_2v64M",
                new Date(new Date().getTime() + (60000 * 4)),
                Game.Console.PLAYSTATION_XBOX,
                "Electronic Arts"));
        games.add(new Game("Mortal Kombat X",
                "Fueled by next-gen technology, Mortal Kombat X combines unparalleled, cinematic presentation with all new gameplay to " +
                        "deliver the most brutal Kombat experience ever. For the first time ever, Mortal Kombat X gives players the ability " +
                        "to choose from multiple variations of each character impacting both strategy and fighting style. And with a new " +
                        "fully-connected gameplay experience, players are launched into a persistent online contest where every fight matters " +
                        "in a global battle for supremacy.",
                "http://upload.wikimedia.org/wikipedia/en/d/d0/Mortal_Kombat_X_Cover_Art.png",
                "https://www.youtube.com/watch?v=8DhubZRnWw4",
                new Date(new Date().getTime() + (60000 * 5)),
                Game.Console.PLAYSTATION_XBOX,
                "Warner Home Video Games"));
        games.add(new Game("The Order: 1886",
                "The Order: 1886 is a deep, story-driven single-player experience that introduces players to a unique alternate history and a" +
                        " dark vision of a 19th century Victorian-Era London. It is the dawn of a new age, an age of scientific marvels, but" +
                        " underneath the dense London fog, in the shadows of the Industrial Revolution, enemies both old and new threaten to " +
                        "throw the city and the world into chaos. Watching over the city are humanity's only protectors, an elite band of Knights " +
                        "known as The Order, and the outcome of this struggle will determine the course of history forever.",
                "http://img2.wikia.nocookie.net/__cb20130612150418/theorder1886/images/7/71/The_Order_1886.jpg",
                "https://www.youtube.com/watch?v=pXOy463Cy88",
                new Date(new Date().getTime() + (60000 * 6)),
                Game.Console.STEAM,
                "Rockstar Games"));

        cb.success(games, getMock200Response());
    }

    private Response getMock200Response() {
        return new Response("mock://", HttpStatus.SC_OK, "plop",
                new ArrayList(0), null);
    }

    private Response getMockFailureResponse() {
        return new Response("mock://", HttpStatus.SC_NOT_FOUND, "plop",
                new ArrayList(0), null);
    }
}
