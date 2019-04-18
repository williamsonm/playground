{ mkDerivation
, aeson, async
, base, bytestring
, configurator, constraints, containers
, directory
, hashable, haxl, http-client, http-types
, lens, lens-aeson
, machines, machines-io, machines-directory
, parallel-io
, random
, stdenv
, tasty, tasty-discover, tasty-hspec, tasty-hunit, tasty-quickcheck
, tardis, time, text
, wreq, zip
, ghcid, hlint, nodejs-10_x, stack, watch
}:
mkDerivation {
  pname = "playground";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = true;
  isExecutable = true;
  buildDepends = [ ghcid hlint nodejs-10_x stack watch ];
  executableHaskellDepends = [
    aeson async
    base bytestring
    configurator constraints containers
    directory
    hashable haxl
    http-client http-types
    lens lens-aeson
    machines
    machines-io
    machines-directory
    parallel-io
    random
    tardis time text
    wreq
  ];
  executableSystemDepends = [ zip ];
  testHaskellDepends = [
    base tasty tasty-discover tasty-hspec tasty-hunit tasty-quickcheck
  ];
  testToolDepends = [ tasty-discover ];
  homepage = "https://github.com/mattheww@pennwell.com/playground#readme";
  license = stdenv.lib.licenses.bsd3;
}
