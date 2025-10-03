import { Detail, LaunchProps } from "@raycast/api";
import { useEffect, useState } from "react";

interface MovieDetails {
  id: number;
  title: string;
  release_date: string;
  overview: string;
  poster_path: string;
  credits: MovieCredits;
}

interface MovieCredits {
  crew: CrewMember[];
}

interface CrewMember {
  id: number;
  name: string;
  job: string;
}

export default function Command(props: LaunchProps<{ arguments: Arguments.SpoonDetails }>) {
  const [movie, setMovie] = useState<MovieDetails | null>(null);

  useEffect(() => {
    async function fetchMovieDetails(movieId: number) {
      const rs = await fetch(`http://localhost:8080/api/movies/${movieId}`);
      console.log(rs);
      const json = await rs.json();
      setMovie(json as MovieDetails);
    }

    const movieId = parseInt((props.arguments as any).ID);

    fetchMovieDetails(movieId);
  }, []);

  return <Detail markdown={movie ? movieDetailsMd(movie) : "# Loading..."} />;
}

function movieDetailsMd(details: MovieDetails) {
  return `

# ${details.title}
### Released in ${new Date(details.release_date).getFullYear()}
### By ${details.credits.crew
    .filter((cm) => cm.job === "Director")
    .map((cm) => cm.name)
    .join(", ")}

![](https://image.tmdb.org/t/p/w185${details.poster_path})

${details.overview}
  `;
}
