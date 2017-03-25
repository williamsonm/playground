module Joy where

-- https://joyofhaskell.com/posts/2017-03-15-typeclasses-in-translation.html

class Semigroup a where
  (<>) :: a -> a -> a

instance Semigroup Int where
  x <> y = x + y

triple :: Semigroup a => a -> a
triple x = x <> x <> x
