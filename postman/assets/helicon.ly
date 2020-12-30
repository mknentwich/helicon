\version "2.20.0"
\language "deutsch"
\pointAndClickOff
\paper {
  top-margin = 10
}
\header {
  title = "Zeitloses Beispiel"
  subtitle = "für Helikon"
  tagline = ##f
}
Melodie = \relative c {
  \time 4/4
  \key des \major
  r4 des,4 as'4. des8 | ces4. b16 as \tuplet 3/2 { b4  as ges } | as4. des,16 des des2 \fermata \bar "|."
}

\book {
  \bookOutputName "trombone"
  \score {
    \new Staff \relative {
      \clef "alto"
      \transpose c, c' \Melodie
    }}
  \header {
    subtitle = "für Altposaune"
  }
}

\book {
  \bookOutputName "helicon"
  \score {
    \new Staff \relative {
      \clef "bass"
      \Melodie
  }}
  \header {
    subtitle = "für Helikon"
  }
}
