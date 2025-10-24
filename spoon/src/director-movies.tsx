import { Action, ActionPanel, Grid, Icon } from "@raycast/api"
import { useEffect, useState } from "react"
import MovieDetails from "./movie-details"

interface MovieItem {
  id: number
  title: string
  originalTitle?: string
  releaseDate: string
  posterUrl?: URL
}

export default function DirectorMovies(props: { directorId: number; directorName: string }) {
  const [movies, setMovies] = useState<MovieItem[] | null>(null)

  useEffect(() => {
    async function fetchMovies(directorId: number) {
      const rs = await fetch(`http://localhost:8080/api/directors/${directorId}/movies`)
      const json = await rs.json()
      setMovies(json as MovieItem[])
    }

    fetchMovies(props.directorId)
  }, [])

  return (
    <Grid isLoading={!movies} navigationTitle={`Movies directed by ${props.directorName}`}>
      {movies &&
        movies.map((movie) => {
          return (
            <Grid.Item
              key={movie.id}
              content={movie.posterUrl?.toString() ?? Icon.CircleDisabled}
              title={movie.title}
              subtitle={movie.releaseDate && new Date(movie.releaseDate).getFullYear().toString()}
              actions={
                <ActionPanel>
                  <Action.Push key={movie.id} title={movie.title} target={<MovieDetails movieId={movie.id} />} />
                </ActionPanel>
              }
            />
          )
        })}
    </Grid>
  )
}
