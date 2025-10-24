import { Detail, useNavigation, open, ActionPanel, Action } from "@raycast/api"
import { useEffect, useState } from "react"
import DirectorMovies from "./director-movies"
import { Director } from "./types/movie-types"

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

  const handleGoToDirectorMovies = (director: Director) => push(<DirectorMovies director={director} />)

  return (
    <Detail
      isLoading={!movie}
      navigationTitle={movie?.title}
      markdown={movie && movieDetailsMd(movie)}
      metadata={
        movie && (
          <Detail.Metadata>
            <Detail.Metadata.TagList title="Directed by">
              {movie.directors.map((director) => (
                <Detail.Metadata.TagList.Item
                  key={director.id}
                  text={director.name}
                  onAction={() => handleGoToDirectorMovies(director)}
                />
              ))}
            </Detail.Metadata.TagList>
            {movie.releaseDate && (
              <Detail.Metadata.Label
                title="Release date"
                text={`${new Date(movie.releaseDate).toLocaleDateString(undefined, { dateStyle: "medium" })}`}
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
      actions={
        movie && (
          <ActionPanel>
            <Action.Push
              title={`Directed by ${movie.directors[0].name}`}
              target={<DirectorMovies director={movie.directors[0]} />}
              shortcut={{ modifiers: ["cmd"], key: "d" }}
            />
          </ActionPanel>
        )
      }
    />
  )
}

function movieDetailsMd(movie: MovieDetails): string {
  const backdrop = movie.backdropUrl ? `![Backdrop](${movie.backdropUrl})` : ""
  const tagline = movie.tagline ? `_${movie.tagline}_` : ""
  const originalTitle = movie.originalTitle ? `## _${movie.originalTitle}_` : ""
  return `
${backdrop}
${tagline}
# ${movie.title}
${originalTitle}
${movie.overview}
`
}
