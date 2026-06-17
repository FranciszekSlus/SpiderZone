import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/app.dart';

void main() {
  testWidgets('SpiderZone app renders home screen', (tester) async {
    await tester.pumpWidget(
      const ProviderScope(
        child: SpiderZoneApp(),
      ),
    );

    await tester.pump();

    expect(find.text('SpiderZone'), findsOneWidget);
  });
}
