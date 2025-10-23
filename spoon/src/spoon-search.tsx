import { LaunchProps, List } from "@raycast/api"
import { useEffect, useState } from "react"

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
  const [movies, setMovies] = useState<MovieSearchResultItem[]>([])

  useEffect(() => {
    async function fetchSearch(q: string) {
      const rs = await fetch(`http://localhost:8080/api/movies/search?q=${q}`)
      const json = await rs.json()
      console.log(json)
      setMovies(json as MovieSearchResultItem[])
    }

    if (query.length > 0) {
      fetchSearch(query)
    }
  }, [query])

  return (
    <List isShowingDetail searchText={query} onSearchTextChange={setQuery} throttle={true}>
      {movies.map((movie) => {
        const posterImage = movie.posterUrl ? `![Poster](${movie.posterUrl})` : ""
        return (
          <List.Item
            id={movie.id.toString()}
            title={movie.title}
            subtitle={movie.releaseDate && new Date(movie.releaseDate).getFullYear().toString()}
            icon={movie.tinyPosterUrl?.toString()}
            detail={
              <List.Item.Detail
                markdown={`
${posterImage}

# ${movie.title}

${movie.overview}
`}
              />
            }
          />
        )
      })}
    </List>
  )
}
