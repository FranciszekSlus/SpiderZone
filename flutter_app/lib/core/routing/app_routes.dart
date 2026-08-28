abstract final class AppRoutes {
  static const home = '/';
  static const species = '/species';
  static const collection = '/collection';
  static const breeding = '/breeding';
  static const profile = '/profile';

  static String speciesDetail(String id) => '/species/$id';
}
