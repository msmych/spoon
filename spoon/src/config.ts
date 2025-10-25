import { environment } from "@raycast/api"

export interface Config {
  apiBaseUrl: string
}

const devConfig: Config = {
  apiBaseUrl: "http://localhost:8080/api",
}

const prodConfig: Config = {
  apiBaseUrl: "https://spoon-app-gug3.onrender.com/api",
}

export const config = environment.isDevelopment ? devConfig : prodConfig
