package com.rentpath.maxleases.data.api;

import com.gf.movie.reminder.data.api.RequestService;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminderSession;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Path;

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

    public void getTrailers(@Path("apiKey") String apiKey, Callback<ArrayList<Movie>> cb) {
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
                "Seth Rogen, Evan Goldberg",
                "James Franco, Seth Rogen, Randall Park ",
                new Date(new Date().getTime() - 60000)));
        movies.add(new Movie("The Hobbit: The Battle of the Five Armies",
                "Bilbo and Company are forced to engage in a war against " +
                        "an array of combatants and keep the Lonely Mountain from falling into the hands of a rising darkness.",
                "http://vignette1.wikia.nocookie.net/lotr/images/f/fe/TBOT5A_Theatrical_Poster.jpg/revision/latest?cb=20141022210753",
                "https://www.youtube.com/watch?v=iVAgTiBrrDA",
                "Peter Jackson",
                "Ian McKellen, Martin Freeman, Richard Armitage, Cate Blanchett",
                new Date(new Date().getTime() + (60000 * 2))));
        movies.add(new Movie("Night at the Museum: Secret of the Tomb",
                "Larry spans the globe, uniting favorite and new characters while embarking on an epic quest to save the " +
                        "magic before it is gone forever.",
                "http://ia.media-imdb.com/images/M/MV5BMjI1MzM2ODEyMV5BMl5BanBnXkFtZTgwNTIzODAwMzE@._V1_SX640_SY720_.jpg",
                "https://www.youtube.com/watch?v=Hr1fFMp0MqU",
                "Shawn Levy",
                "Ben Stiller, Robin Williams, Owen Wilson, Dick Van Dyke",
                new Date(new Date().getTime() + (60000 * 3))));
        movies.add(new Movie("The Hunger Games: Mockingjay - Part 1",
                "When Katniss destroys the games, she goes to District 13 after District 12 is destroyed. She meets President " +
                        "Coin who convinces her to be the symbol of rebellion, while trying to save Peeta from the Capitol.",
                "http://imageserver.moviepilot.com/hungergamesmockingjay-the-hunger-games-mockingjay-part-1-hits-300-million.jpeg?width=720&height=1110",
                "https://www.youtube.com/watch?v=3PkkHsuMrho",
                "Francis Lawrence",
                "Jennifer Lawrence, Josh Hutcherson, Liam Hemsworth, Woody Harrelson",
                new Date(new Date().getTime() + (60000 * 4))));
        movies.add(new Movie("Exodus: Gods and Kings",
                "The defiant leader Moses rises up against the Egyptian Pharaoh Ramses, setting 600,000 slaves on a monumental " +
                        "journey of escape from Egypt and its terrifying cycle of deadly plagues.",
                "https://s.yimg.com/cd/resizer/FIT_TO_WIDTH-w1383/10c4408ed1ac4b3d825a6ade7b9607a96ed906be.jpg",
                "https://www.youtube.com/watch?v=t-8YsulfxVI",
                "Ridley Scott",
                "Christian Bale, Joel Edgerton, Ben Kingsley, Sigourney Weaver",
                new Date(new Date().getTime() + (60000 * 5))));
        movies.add(new Movie("Interstellar",
                "A team of explorers travel through a wormhole in an attempt to ensure humanity's survival.",
                "http://ia.media-imdb.com/images/M/MV5BMjIxNTU4MzY4MF5BMl5BanBnXkFtZTgwMzM4ODI3MjE@._V1_SX640_SY720_.jpg",
                "https://www.youtube.com/watch?v=0vxOhd4qlnA",
                "Christopher Nolan",
                "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Mackenzie Foy",
                new Date(new Date().getTime() + (60000 * 6))));

        cb.success(movies, getMock200Response());
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
