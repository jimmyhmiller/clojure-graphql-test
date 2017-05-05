# clj-graphql

FIXME

## Prerequisites

You will need Java ([instructions for macOS](https://medium.com/@sgnl/installing-clojure-on-osx-eb3bf77908c9)) and [Leiningen](https://github.com/technomancy/leiningen) 2.0.0 or above installed.

## Running

To start a web server for the application, run:

    lein ring server
    
## Sample Query

```graphql
{
  hero(episode: JEDI) {
    name
    appears_in
  }
}
```

## License

Copyright © 2017 FIXME
