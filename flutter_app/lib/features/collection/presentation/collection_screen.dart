import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:spiderzone/core/media/cloudinary_upload_service.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/core/widgets/sz_primary_button.dart';

class CollectionScreen extends StatefulWidget {
  const CollectionScreen({super.key});

  @override
  State<CollectionScreen> createState() => _CollectionScreenState();
}

class _CollectionScreenState extends State<CollectionScreen> {
  final _nameController = TextEditingController();
  final _picker = ImagePicker();
  final _uploadService = CloudinaryUploadService();
  final _animals = <_DraftAnimal>[];

  CloudinaryUploadResult? _uploadedPhoto;
  bool _isUploading = false;
  String? _error;

  @override
  void dispose() {
    _nameController.dispose();
    super.dispose();
  }

  Future<void> _pickAndUploadPhoto() async {
    setState(() {
      _error = null;
      _isUploading = true;
    });

    try {
      final image = await _picker.pickImage(
        source: ImageSource.gallery,
        imageQuality: 85,
      );

      if (image == null) {
        return;
      }

      final result = await _uploadService.uploadImage(image);
      if (!mounted) return;

      setState(() => _uploadedPhoto = result);
    } catch (error) {
      if (!mounted) return;
      setState(() => _error = error.toString());
    } finally {
      if (mounted) {
        setState(() => _isUploading = false);
      }
    }
  }

  void _addAnimal() {
    final name = _nameController.text.trim();
    final photo = _uploadedPhoto;

    if (name.isEmpty) {
      setState(() => _error = 'Podaj nazwę zwierzęcia.');
      return;
    }
    if (photo == null) {
      setState(() => _error = 'Najpierw dodaj zdjęcie.');
      return;
    }

    setState(() {
      _animals.insert(
        0,
        _DraftAnimal(
          name: name,
          photoUrl: photo.secureUrl,
          publicId: photo.publicId,
        ),
      );
      _nameController.clear();
      _uploadedPhoto = null;
      _error = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: ListView(
        padding: const EdgeInsets.all(AppTokens.paddingScreen),
        children: [
          Text(
            'Moja hodowla',
            style: Theme.of(context).textTheme.titleLarge?.copyWith(
                  color: AppColors.textPrimary,
                  fontWeight: FontWeight.bold,
                ),
          ),
          const SizedBox(height: 8),
          const Text(
            'Minimalne dodawanie zwierzęcia do testu Cloudinary. Dane są lokalne i znikną po restarcie aplikacji.',
            style: TextStyle(color: AppColors.textSecondary),
          ),
          const SizedBox(height: 16),
          SzCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'Dodaj zwierzę testowo',
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 12),
                TextField(
                  controller: _nameController,
                  decoration: const InputDecoration(
                    labelText: 'Nazwa zwierzęcia',
                    hintText: 'np. Genowefa',
                  ),
                ),
                const SizedBox(height: 12),
                SzPrimaryButton(
                  label: _isUploading
                      ? 'Wysyłanie zdjęcia...'
                      : 'Wybierz i wyślij zdjęcie',
                  onPressed: _isUploading ? null : _pickAndUploadPhoto,
                ),
                if (_uploadedPhoto != null) ...[
                  const SizedBox(height: 12),
                  ClipRRect(
                    borderRadius: BorderRadius.circular(AppTokens.radiusCard),
                    child: Image.network(
                      _uploadedPhoto!.secureUrl,
                      height: 180,
                      width: double.infinity,
                      fit: BoxFit.cover,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    _uploadedPhoto!.secureUrl,
                    style: const TextStyle(
                      color: AppColors.textSecondary,
                      fontSize: 12,
                    ),
                  ),
                ],
                if (_error != null) ...[
                  const SizedBox(height: 12),
                  Text(
                    _error!,
                    style: const TextStyle(color: AppColors.danger),
                  ),
                ],
                const SizedBox(height: 12),
                SzPrimaryButton(
                  label: 'Dodaj do hodowli',
                  onPressed: _isUploading ? null : _addAnimal,
                  variant: SzButtonVariant.secondary,
                ),
              ],
            ),
          ),
          const SizedBox(height: 16),
          if (_animals.isEmpty)
            const Text(
              'Nie masz jeszcze testowych zwierząt.',
              style: TextStyle(color: AppColors.textSecondary),
            )
          else
            ..._animals.map((animal) => _AnimalCard(animal: animal)),
        ],
      ),
    );
  }
}

class _AnimalCard extends StatelessWidget {
  const _AnimalCard({required this.animal});

  final _DraftAnimal animal;

  @override
  Widget build(BuildContext context) {
    return SzCard(
      child: Row(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(12),
            child: Image.network(
              animal.photoUrl,
              height: 76,
              width: 76,
              fit: BoxFit.cover,
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  animal.name,
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 4),
                Text(
                  animal.publicId,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(
                    color: AppColors.textSecondary,
                    fontSize: 12,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _DraftAnimal {
  const _DraftAnimal({
    required this.name,
    required this.photoUrl,
    required this.publicId,
  });

  final String name;
  final String photoUrl;
  final String publicId;
}
