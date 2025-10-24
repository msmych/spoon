import { Action, ActionPanel, Icon, LaunchProps, List } from "@raycast/api"
import { useEffect, useState } from "react"
import MovieDetails from "./movie-details"

interface MovieSearchResultItem {
  id: number
  title: string
  originalTitle?: string
  overview: string
  releaseDate?: string
  posterUrl?: URL
  tinyPosterUrl?: URL
}

export default function Command(props: LaunchProps<{ arguments: Arguments.SpoonSearch }>) {
  const initialQuery = (props.arguments as any).Query
  const [query, setQuery] = useState<string>(initialQuery)
  const [movies, setMovies] = useState<MovieSearchResultItem[] | null>(null)

  useEffect(() => {
    async function fetchSearch(q: string) {
      const rs = await fetch(`http://localhost:8080/api/movies/search?q=${q}`)
      const json = await rs?.json()
      setMovies(json as MovieSearchResultItem[])
    }

    if (query.length > 0) {
      fetchSearch(query)
    }
  }, [query])

  return (
    <List isShowingDetail isLoading={!movies} searchText={query} onSearchTextChange={setQuery} throttle={true}>
      {movies &&
        movies.map((movie) => {
          const posterImage = movie.posterUrl ? `![Poster](${movie.posterUrl}?raycast-height=256)` : ""
          const originalTitle = movie.originalTitle ? `## _${movie.originalTitle}_` : ""
          return (
            <List.Item
              id={movie.id.toString()}
              key={movie.id}
              title={{ value: movie.title, tooltip: movie.originalTitle }}
              subtitle={movie.releaseDate && new Date(movie.releaseDate).getFullYear().toString()}
              icon={movie.tinyPosterUrl?.toString() ?? Icon.BlankDocument}
              detail={
                <List.Item.Detail
                  markdown={`
${posterImage}
# ${movie.title}
${originalTitle}
${movie.overview}
`}
                />
              }
              actions={
                <ActionPanel>
                  <Action.Push key={movie.id} title={movie.title} target={<MovieDetails movieId={movie.id} />} />
                </ActionPanel>
              }
            />
          )
        })}
    </List>
  )
}
