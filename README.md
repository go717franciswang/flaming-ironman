# parse-tree

* Display parse tree with [C2]

## Development

```sh
lein cljsbuild auto
rlwrap -r -m -q '\\"' -b "(){}[],^%3@\\\";:'" lein trampoline cljsbuild repl-listen
```

### TODO
* c2's bind function raises exception when tree changes in structure. Find a way to
  solve it.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[C2]: https://github.com/lynaghk/c2
