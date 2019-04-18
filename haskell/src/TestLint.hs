module TestLint where

kcomp :: Monad m => (b -> m c) -> (a -> m b) -> a -> m c
-- kcomp k k1 = \x -> k1 x >>= k
kcomp k k1 x = k1 x >>= k
