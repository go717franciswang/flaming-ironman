# explore-c2

Explore basic functionalities of [C2]

## Development

```sh
lein cljsbuild auto
rlwrap -r -m -q '\\"' -b "(){}[],^%3@\\\";:'" lein trampoline cljsbuild repl-listen
```

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[C2]: https://github.com/lynaghk/c2
