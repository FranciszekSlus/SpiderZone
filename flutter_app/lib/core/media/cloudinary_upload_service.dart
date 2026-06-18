import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';

class CloudinaryUploadResult {
  const CloudinaryUploadResult({
    required this.publicId,
    required this.secureUrl,
    required this.bytes,
    required this.format,
  });

  final String publicId;
  final String secureUrl;
  final int bytes;
  final String format;
}

class CloudinaryUploadService {
  CloudinaryUploadService({
    http.Client? client,
    this.cloudName = 'duxun3ylu',
    this.uploadPreset = 'spiderzone_dev_unsigned',
  }) : _client = client ?? http.Client();

  final http.Client _client;
  final String cloudName;
  final String uploadPreset;

  Uri get _uploadUri =>
      Uri.https('api.cloudinary.com', '/v1_1/$cloudName/image/upload');

  Future<CloudinaryUploadResult> uploadImage(XFile image) async {
    final bytes = await image.readAsBytes();
    final request = http.MultipartRequest('POST', _uploadUri)
      ..fields['upload_preset'] = uploadPreset
      ..fields['folder'] = 'spiderzone/dev'
      ..files.add(
        http.MultipartFile.fromBytes(
          'file',
          bytes,
          filename: image.name,
        ),
      );

    final response = await _client.send(request);
    final body = await response.stream.bytesToString();

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw CloudinaryUploadException(
        statusCode: response.statusCode,
        body: body,
      );
    }

    final json = jsonDecode(body) as Map<String, dynamic>;
    return CloudinaryUploadResult(
      publicId: json['public_id'] as String? ?? '',
      secureUrl: json['secure_url'] as String? ?? '',
      bytes: json['bytes'] as int? ?? 0,
      format: json['format'] as String? ?? '',
    );
  }
}

class CloudinaryUploadException implements Exception {
  const CloudinaryUploadException({
    required this.statusCode,
    required this.body,
  });

  final int statusCode;
  final String body;

  @override
  String toString() => 'Cloudinary upload failed ($statusCode): $body';
}
