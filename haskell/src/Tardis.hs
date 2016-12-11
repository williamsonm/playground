-- http://stackoverflow.com/questions/24414700/amazon-water-collected-between-towers
-- https://gist.github.com/paf31/9d84ecf6a6a9b69cdb597a390f25764d - Phil Freeman

module Tardis where

import Control.Monad.Tardis

levels :: [Integer] -> [Integer]
levels hs =
  evalTardis (traverse
    (\h -> do x <- min <$> getFuture <*> getPast
              modifyBackwards (`max` h)
              modifyForwards (`max` h)
              pure (max 0 (x - h))
    ) hs) (0,0)

result :: Integer
result = sum $ levels [5,3,7,2,6,4,5,9,1,2]
-- result = 14
