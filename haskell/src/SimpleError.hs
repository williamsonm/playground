module SimpleError where

import Control.Monad

parse :: MonadPlus m => String -> m Int
parse s =
  if s == "wut"
    then pure 5
    else mzero -- (fail "failed to parse")

parseMaybe :: String -> Maybe Int
parseMaybe = parse

-- parseEither :: String -> Either String Int
-- parseEither = parse

-- usage
main :: IO ()
main = do
  a <- parse "wut"
  print a
  b <- parse "nope"
  print b
  print (a + b)
