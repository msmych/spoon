import { Detail, LaunchProps } from "@raycast/api"
import { useEffect, useState } from "react"

interface MovieDetails {
  id: number
  title: string
  release_date: string
  overview: string
  poster_path: string
  credits: MovieCredits
}

interface MovieCredits {
  crew: CrewMember[]
}

interface CrewMember {
  id: number
  name: string
  job: string
}

export default function Command(props: LaunchProps<{ arguments: Arguments.SpoonDetails }>) {
  const [movie, setMovie] = useState<MovieDetails | null>(null)

  useEffect(() => {
    async function fetchMovieDetails(movieId: number) {
      const rs = await fetch(`http://localhost:8080/api/movies/${movieId}`)
      console.log(rs)
      const json = await rs.json()
      setMovie(json as MovieDetails)
    }

    const movieId = parseInt((props.arguments as any).ID)

    fetchMovieDetails(movieId)
  }, [])

  return (
    <Detail
      isLoading={!movie}
      navigationTitle={movie?.title ?? "No title"}
      markdown={movie ? movieDetailsMd(movie) : "# Loading..."}
      metadata={
        movie && (
          <Detail.Metadata>
            <Detail.Metadata.Label title="Release date" text={`${new Date(movie.release_date).toLocaleDateString()}`} />
            <Detail.Metadata.TagList title="Directed by">
              {movie.credits.crew
                .filter((member) => member.job === "Director")
                .map((member) => (
                  <Detail.Metadata.TagList.Item text={member.name} />
                ))}
            </Detail.Metadata.TagList>
            <Detail.Metadata.Separator />
          </Detail.Metadata>
        )
      }
    />
  )
}

function movieDetailsMd(movie: MovieDetails) {
  return `
# ${movie.title}

![](https://image.tmdb.org/t/p/w185${movie.poster_path})

${movie.overview}
`
}
