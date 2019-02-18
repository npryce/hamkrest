# Version Numbering

Hamkrest releases have version numbers with the following scheme:

    {mental}.{major}.{minor}.{patch}

Major, minor and patch follow [Semantic Versioning](https://semver.org/) conventions.  "Mental" is incremented when there is a change to the API that significantly changes it's conceptual model.  It is incremeneted with the release is not just incompatible with previous releases, but also that users will have to change the way they think about using the library in their designs.
