module Printf where

-- http://kcsongor.github.io/purescript-safe-printf/

-- foreign import === types without constructors
foreign import kind Specifier
foreign import data D :: Specifier
foreign import data S :: Specifier
foreign import data Lit :: Symbol -> Specifier

foreign import kind FList
foreign import data FNil :: FList
foreign import data FCons :: Specifier -> FList -> FList

-- FCons D (FCons (Lit " foo") FNil)

class Parse (string :: Symbol) (format :: FList) | string -> format

class ConsSymbol (head :: Symbol)
                 (tail :: Symbol)
                 (sym :: Symbol) |
                 sym -> head tail, head tail -> sym

class MatchFmt (head :: Symbol) (out :: Specifier) | head -> out
instance matchFmtD :: MatchFmt "d" D
instance matchFmtS :: MatchFmt "s" S

class Match (head :: Symbol) (tail :: Symbol) (out :: FList) | head tail -> out

instance aMatchFmt :: Match a "" (FCons (Lit a) FNil)
else instance bMatchFmt ::
  ( ConsSymbol h t s
  , MatchFmt h spec
  , Parse t rest
  ) => Match "%" s (FCons (Lit "") (FCons spec rest))
else instance cMatchFmt ::
  ( Parse s (FCons (Lit acc) r)
  , ConsSymbol o acc rest
  ) => Match o s (FCons (List rest) r)

instance aNilParse :: Parse "" (FCons (Lit "") FNil)
else instance bConsParse :: (ConsSymbol h t string, Match h t fl) => Parse string fl
