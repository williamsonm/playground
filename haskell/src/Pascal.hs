module Pascal where

--     1
--    1 1
--   1 2 1
--  1 3 3 1
-- 1 4 6 4 1

pascal :: (Num a, Ord a) => a -> a -> a
pascal _ 0 = 1
pascal 0 _ = 1
pascal c r
  | c < 0 || r < 0 = 0
  | c == r = 1
  | otherwise = pascal c (r - 1) + pascal (c - 1) (r - 1)
