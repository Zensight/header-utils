# header-utils

[![Clojars][clojars-img]][clojars-url]
[![Build Status][travis-image]][travis-url]
[![MIT License][license-image]][license]
![Phasers to stun][phasers-image]

A Clojure library for handling gross things in HTTP headers. Specifically, it can encode and parse Content-Disposition headers UTF-8 characters (RFC 6266), and exposes clean APIs for working with RFC 5987 encodings generally.

## Usage

### Content-Disposition

```clj
user=> (use 'header-utils.content-disposition)

user=> (def s (encode "attachment" "Y͢o҉u f̴ee̡l̡ ̶fée͝bl̢e.͡.pdf"))

user=> s
"attachment;filename*=UTF-8''Y%CD%A2o%D2%89u%20f%CC%B4ee%CC%A1l%CC%A1%20%CC%B6f%C3%A9e%CD%9Dbl%CC%A2e.%CD%A1.pdf"

user=> (parse-type s)
"attachment"

user=> (parse-filename s)
"Y͢o҉u f̴ee̡l̡ ̶fée͝bl̢e.͡.pdf"
```

You can also specify language and additional parameters.

### Other tools

My goal in writing this library was the handle Content-Disposition, but I took some pains to make the proximate tools as useful as possible. Specifically:

 * `header-utils.parameters` - encode and parse RFC 5987 parameters
 * `header-utils.encoding` - common tools for reading/writing header values
 * `header-utils.parser` - internal tool useful in extending the library (e.g. adding direct support for additional headers)

## Todo

This library contains all the utilities required for adding explicit support for other headers, and actually doing so should be relatively easy. I'll add that support if/when I need them or you send me a PR.

## License

Copyright © 2015 Zensight

Distributed under the MIT License. See [LICENSE][] for more info.

[documentation-url]: http://icambron.github.io/twix.js/docs.html

[license-image]: http://img.shields.io/badge/license-MIT-blue.svg?style=flat-square
[license]: LICENSE

[clojars-url]: https://clojars.org/co.zensight/header-utils
[clojars-img]: https://img.shields.io/clojars/v/co.zensight/header-utils.svg?style=flat-square

[travis-url]: http://travis-ci.org/Zensight/header-utils
[travis-image]: http://img.shields.io/travis/Zensight/header-utils.svg?style=flat-square

[phasers-image]: https://img.shields.io/badge/phasers-stun-brightgreen.svg?style=flat-square
