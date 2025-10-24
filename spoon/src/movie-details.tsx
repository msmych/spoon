import { Detail, useNavigation, open } from "@raycast/api"
import { useEffect, useState } from "react"
import DirectorMovies from "./director-movies"

interface MovieDetails {
  id: number
  title: string
  originalTitle?: string
  runTime: string
  releaseDate?: string
  overview: string
  tagline?: string
  posterUrl?: URL
  backdropUrl?: URL
  directors: Director[]
  genres: string[]
  links: Map<string, URL>
}

interface Director {
  id: number
  name: string
}

export default function MovieDetails(props: { movieId: number }) {
  const [movie, setMovie] = useState<MovieDetails | null>(null)

  useEffect(() => {
    async function fetchMovieDetails(movieId: number) {
      const rs = await fetch(`http://localhost:8080/api/movies/${movieId}`)
      const json = await rs.json()
      setMovie(json as MovieDetails)
    }

    fetchMovieDetails(props.movieId)
  }, [])

  const { push } = useNavigation()

  return (
    <Detail
      isLoading={!movie}
      navigationTitle={movie?.title ?? "No title"}
      markdown={movie ? movieDetailsMd(movie) : "# Loading..."}
      metadata={
        movie && (
          <Detail.Metadata>
            <Detail.Metadata.TagList title="Directed by">
              {movie.directors.map((director) => (
                <Detail.Metadata.TagList.Item
                  key={director.id}
                  text={director.name}
                  onAction={() => push(<DirectorMovies directorId={director.id} directorName={director.name} />)}
                />
              ))}
            </Detail.Metadata.TagList>
            {movie.releaseDate && (
              <Detail.Metadata.Label
                title="Release date"
                text={`${new Date(movie.releaseDate).toLocaleDateString()}`}
              />
            )}
            <Detail.Metadata.Label title="Run time" text={movie.runTime} />
            <Detail.Metadata.Separator />
            <Detail.Metadata.Label title="Genres" text={movie.genres.join(", ")} />
            <Detail.Metadata.TagList title="Links">
              {Object.entries(movie.links).map(([k, v]) => (
                <Detail.Metadata.TagList.Item key={k} text={k} onAction={() => open(v)} />
              ))}
            </Detail.Metadata.TagList>
          </Detail.Metadata>
        )
      }
    />
  )
}

function movieDetailsMd(movie: MovieDetails): string {
  const backdrop = movie.backdropUrl ? `![Backdrop](${movie.backdropUrl})` : ""
  const tagline = movie.tagline ? `_${movie.tagline}_` : ""
  const originalTitle = movie.originalTitle ? `## ${movie.originalTitle}` : ""
  return `
${backdrop}
${tagline}
# ${movie.title}
${originalTitle}
${movie.overview}
`
}
